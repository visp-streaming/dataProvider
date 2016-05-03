package vispDataProvider.job;


import entities.Message;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.dataSender.RabbitMQSender;

@PersistJobDataAfterExecution
public class SequentialWaitGeneratorJob extends DataGeneratorJob {

    @Autowired
    protected RabbitMQSender sender;

    protected void customDataGeneration() {

        for (int i = 0; i<state.getAmount(); i++) {
            Message msg = new Message("wait", "step1");
            ConnectionThread con1 = new ConnectionThread(sender, msg);
            new Thread(con1).start();
        }
    }

}
