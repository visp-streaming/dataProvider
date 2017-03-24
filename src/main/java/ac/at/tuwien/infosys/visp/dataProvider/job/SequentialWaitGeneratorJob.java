package ac.at.tuwien.infosys.visp.dataProvider.job;


import ac.at.tuwien.infosys.visp.dataProvider.dataSender.RabbitMQSender;
import org.springframework.amqp.core.Message;

public class SequentialWaitGeneratorJob extends DataGeneratorJob {

    protected RabbitMQSender sender;

    public void customDataGeneration() {
        sender = new RabbitMQSender(host, user, password);

        for (int i = 0; i<state.getAmount(); i++) {
            Message msg = createMessage("wait", "step1");
            ConnectionThread con1 = new ConnectionThread(sender, msg, "source");
            new Thread(con1).start();
        }
    }

}
