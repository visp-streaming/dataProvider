package vispDataProvider.job;


import entities.Message;
import vispDataProvider.dataSender.RabbitMQSender;

public class SequentialWaitGeneratorJob extends DataGeneratorJob {

    protected RabbitMQSender sender;

    public void customDataGeneration() {
        sender = new RabbitMQSender(host, user, password);

        for (int i = 0; i<state.getAmount(); i++) {
            Message msg = new Message("wait", "step1");
            ConnectionThread con1 = new ConnectionThread(sender, msg, "source");
            new Thread(con1).start();
        }
    }

}
