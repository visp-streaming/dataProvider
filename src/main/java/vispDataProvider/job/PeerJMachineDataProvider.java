package vispDataProvider.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Message;
import entities.peerJ.MachineData;
import entities.peerJ.Status;
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

        Integer reportingInterval = 60;

        Map<String, String> assets = new HashMap<String, String>()
        {{
            put("m1", "location1");
            put("m2", "location1");
            put("m3", "location2");
        }};

        for (Map.Entry<String, String> entry : assets.entrySet()) {
            String assetID = entry.getKey();
            String location = entry.getValue();

        MachineData md = new MachineData();
        md.setId(UUID.randomUUID().toString());
        md.setAssetID(assetID);
        md.setMachineType("Robot " + assetID);
        md.setLocation(location);

        dt = dt.plusSeconds(reportingInterval);
        md.setTimestamp(dt.toString());
        md.setPlannedProductionTime(reportingInterval/3);


        if ((int)(Math.random() * 10) == 1) {
            md.setProducedUnits(0);
            md.setProducedUnits(0);

            if ((int)(Math.random() * 5) == 1) {
                md.setActive(Status.PLANNEDDOWNTIME);
            } else {
                md.setActive(Status.DEFECT);
            }
        } else {
            md.setProducedUnits(ThreadLocalRandom.current().nextInt(0, 3 + 2 + 1));
            md.setProducedUnits(Math.abs(ThreadLocalRandom.current().nextInt(0, 2)-md.getProducedUnits()));
        }

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Message msg = null;
            try {
                msg = new Message("machineData", ow.writeValueAsString(md));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ConnectionThread con1 = new ConnectionThread(sender, msg);
            new Thread(con1).start();
        }

        jdMap.put("dateTimeMachineData", dt.toString());

    }
}
