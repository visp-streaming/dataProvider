package vispDataProvider.dataSender;

import entities.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(queue);
        template.setQueue(queue);
        template.convertAndSend(queue, queue, msg);

        connectionFactory.destroy();
    }

}
