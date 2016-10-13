package vispDataProvider.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Message;
import entities.peerJ.Temperature;
import org.joda.time.DateTime;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.dataSender.RabbitMQSender;

import java.util.Arrays;

@PersistJobDataAfterExecution
public class PeerJTemperatureProvider extends DataGeneratorJob {

    @Autowired
    protected RabbitMQSender sender;

    public void customDataGeneration() {

        Integer reportingInterval = 1;

        DateTime dt = new DateTime();

        if (jdMap.get("lastTime") != null) {
            dt = new DateTime(jdMap.get("dateTimeTemperature").toString());
        }

        for (String assetID : Arrays.asList("m1", "m2", "m3")) {

            Temperature temp = new Temperature();
            dt = dt.plusSeconds(reportingInterval);
            temp.setTimestamp(dt.toString());
            temp.setAssetID(assetID);

            temp.setTemperature((int)(Math.random() * 102));

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Message msg = null;
            try {
                msg = new Message("temperature", ow.writeValueAsString(temp));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ConnectionThread con1 = new ConnectionThread(sender, msg);
            new Thread(con1).start();

        }

        jdMap.put("dateTimeTemperature", dt.toString());
    }
}
