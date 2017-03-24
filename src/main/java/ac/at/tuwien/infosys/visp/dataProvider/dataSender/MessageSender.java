package ac.at.tuwien.infosys.visp.dataProvider.dataSender;


import org.springframework.amqp.core.Message;

public interface MessageSender {

    void sendMessage(Message msg, String queue);

}
