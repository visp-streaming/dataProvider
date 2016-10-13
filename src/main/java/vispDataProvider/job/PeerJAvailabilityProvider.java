package vispDataProvider.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Message;
import entities.peerJ.Availability;
import entities.peerJ.Status;
import org.joda.time.DateTime;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.dataSender.RabbitMQSender;

import java.util.Arrays;

@PersistJobDataAfterExecution
public class PeerJAvailabilityProvider extends DataGeneratorJob {

    @Autowired
    protected RabbitMQSender sender;

    public void customDataGeneration() {

        Integer reportingInterval = 1;

        DateTime dt = new DateTime();

        if (jdMap.get("lastTime") != null) {
            dt = new DateTime(jdMap.get("dateTimeAvailability").toString());
        }

        for (String assetID : Arrays.asList("m1", "m2", "m3")) {

            Availability av = new Availability();
            dt = dt.plusSeconds(reportingInterval);
            av.setTimestamp(dt.toString());
            av.setAssetID(assetID);

            if ((int) (Math.random() * 100) == 1) {
                av.setAvailability(Status.DEFECT);
            } else {
                if ((int) (Math.random() * 50) == 1) {
                    av.setAvailability(Status.PLANNEDDOWNTIME);
                } else {
                    av.setAvailability(Status.DEFECT);
                }
            }

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Message msg = null;
            try {
                msg = new Message("availability", ow.writeValueAsString(av));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ConnectionThread con1 = new ConnectionThread(sender, msg);
            new Thread(con1).start();

        }

        jdMap.put("dateTimeAvailability", dt.toString());
    }
}
