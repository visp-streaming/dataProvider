package ac.at.tuwien.infosys.visp.dataProvider.dataSender;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Date;

public class RabbitMQSender implements MessageSender {

    private String host;
    private String username;
    private String password;

    public RabbitMQSender(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public void sendMessage(Message msg, String queue) {

        msg.getMessageProperties().setTimestamp(new Date());

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(queue);
        template.setQueue(queue);
        template.send(queue, queue, msg);

        //TODO close connection after template is sent

        //TODO redeploy topology
        connectionFactory.resetConnection();
        connectionFactory.destroy();

    }

}
