package vispDataProvider.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Message;
import entities.peerJ.Availability;
import entities.peerJ.MachineData;
import entities.peerJ.Temperature;
import org.joda.time.DateTime;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.dataSender.RabbitMQSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@PersistJobDataAfterExecution
public class PeerJMachineDataProvider extends DataGeneratorJob {

    @Autowired
    protected RabbitMQSender sender;

    public void customDataGeneration() {

        DateTime dt = new DateTime();

        if (jdMap.get("lastTime") != null) {
            dt = new DateTime(jdMap.get("dateTimeMachineData").toString());
        }

        //needs to be a multiple of 10
        Integer reportingInterval = 60;

        Map<String, String> assets = new HashMap<String, String>() {{
            put("m1", "location1");
            put("m2", "location1");
            put("m3", "location2");
        }};

        for (Map.Entry<String, String> entry : assets.entrySet()) {

            String assetID = entry.getKey();
            String location = entry.getValue();

            Integer steps = (int) reportingInterval / 10;
            Integer intermediateIntervall = 0;

            for (int i = 0; i < steps; i++) {
                //generate Temperature Data

                Temperature temp = new Temperature();
                dt = dt.plusSeconds(intermediateIntervall);
                temp.setTimestamp(dt.toString());
                temp.setAssetID(assetID);

                temp.setTemperature((int) (Math.random() * 102));

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                Message msg1 = null;
                try {
                    msg1 = new Message("temperature", ow.writeValueAsString(temp));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                ConnectionThread con1 = new ConnectionThread(sender, msg1, "sourcetemperature");
                new Thread(con1).start();

                //generate Availability Data
                Availability av = new Availability();
                dt = dt.plusSeconds(intermediateIntervall);
                av.setTimestamp(dt.toString());
                av.setAssetID(assetID);

                if ((int) (Math.random() * 100) == 1) {
                    av.setAvailability("DEFECT");
                } else {
                    if ((int) (Math.random() * 50) == 1) {
                        av.setAvailability("PLANNEDDOWNTIME");
                    } else {
                        av.setAvailability("DEFECT");
                    }
                }

                Message msg2 = null;
                try {
                    msg2 = new Message("availability", ow.writeValueAsString(av));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                ConnectionThread con2 = new ConnectionThread(sender, msg2, "sourceavailability");
                new Thread(con2).start();

                intermediateIntervall+=10;
            }


            MachineData md = new MachineData();
            md.setId(UUID.randomUUID().toString());
            md.setAssetID(assetID);
            md.setMachineType("Robot " + assetID);
            md.setLocation(location);

            dt = dt.plusSeconds(reportingInterval);
            md.setTimestamp(dt.toString());
            md.setPlannedProductionTime(reportingInterval / 3);


            if ((int) (Math.random() * 10) == 1) {
                md.setProducedUnits(0);
                md.setDefectiveUnits(0);

                if ((int) (Math.random() * 5) == 1) {
                    md.setActive("PLANNEDDOWNTIME");
                } else {
                    md.setActive("DEFECT");
                }
            } else {
                md.setActive("ACTIVE");
                md.setProducedUnits(ThreadLocalRandom.current().nextInt(0, 3 + 2 + 1));
                md.setDefectiveUnits(Math.abs(ThreadLocalRandom.current().nextInt(0, 2) - md.getProducedUnits()));
            }

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Message msg3 = null;
            try {
                msg3 = new Message("initialmachinedata", ow.writeValueAsString(md));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ConnectionThread con3 = new ConnectionThread(sender, msg3, "sourcemachinedata");
            new Thread(con3).start();
        }

        jdMap.put("dateTimeMachineData", dt.toString());

    }
}
