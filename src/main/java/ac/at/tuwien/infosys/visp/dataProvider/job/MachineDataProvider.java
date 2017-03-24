package ac.at.tuwien.infosys.visp.dataProvider.job;

import ac.at.tuwien.infosys.visp.common.peerJ.Availability;
import ac.at.tuwien.infosys.visp.common.peerJ.MachineData;
import ac.at.tuwien.infosys.visp.common.peerJ.Temperature;
import ac.at.tuwien.infosys.visp.dataProvider.dataSender.RabbitMQSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.amqp.core.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MachineDataProvider extends DataGeneratorJob {

    protected RabbitMQSender sender;

    private Integer offset = 0;

    public void customDataGeneration() {
        sender = new RabbitMQSender(host, user, password);

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

            Integer steps = reportingInterval / 10;
            Integer intermediateIntervall = 0;

            for (int i = 0; i < steps; i++) {
                //generate Temperature Data

                Temperature temp = new Temperature();
                dt = dt.plusSeconds(intermediateIntervall);
                temp.setTimestamp(dt.toString());
                temp.setAssetID(assetID);

                temp.setTemperature((int) (Math.random() * 102));

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                Message msg1 = createEmptyMessage();
                try {
                    msg1 = createMessage("temperature", ow.writeValueAsBytes(temp));
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
                    msg2 = createMessage("availability", ow.writeValueAsBytes(av));
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

            Message msg3 = null;
            try {
                String generatedImagePath = generateImage(md);
                byte[] imageBytes = FileUtils.readFileToByteArray(new File(generatedImagePath));
                msg3 = createMessage("initialmachinedata", imageBytes);
                Files.deleteIfExists(Paths.get(generatedImagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ConnectionThread con3 = new ConnectionThread(sender, msg3, "sourcemachinedata");
            new Thread(con3).start();
        }

        jdMap.put("dateTimeMachineData", dt.toString());

    }

    private String generateImage(MachineData md) throws IOException {
        offset = 0;

        String script = "#!/bin/bash \n";
        script += "convert -size 850x300 xc:white  -pointsize 30 ";
        script += addText("assetID", md.getAssetID());
        script += addText("machinetype", md.getMachineType());
        script += addText("location", md.getLocation());
        script += addText("producedunits", String.valueOf(md.getProducedUnits()));
        script += addText("defectiveunits", String.valueOf(md.getDefectiveUnits()));
        script += addText("plannedproductiontime", String.valueOf(md.getPlannedProductionTime()));
        script += addText("active", md.getActive());
        script += addText("timestamp", md.getTimestamp());

        File imageFile = File.createTempFile("generatedImage", ".png");

        script += imageFile.getAbsolutePath();

        File scriptFile = File.createTempFile("generationscript", ".sh");

        try {
            OutputStream os = new FileOutputStream(scriptFile);
            os.write(script.getBytes(), 0, script.length());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Process pr1 = Runtime.getRuntime().exec("chmod 777 " + scriptFile.getAbsolutePath());
            pr1.waitFor();

            Process pr2 = Runtime.getRuntime().exec("" + scriptFile.getAbsolutePath());
            pr2.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scriptFile.delete();

        return imageFile.getAbsolutePath();
    }

    private String addText(String identifier, String value) {
        offset += 32;
        return "-fill black -draw \"text 10," + offset + " '" + identifier + "  :  " + value + "'\" ";
    }


}
