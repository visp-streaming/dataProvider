package vispDataProvider.dataSender;

import entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender implements MessageSender {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSender.class);

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;


    public void sendMessage(Message msg, String queue) {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(queue);
        template.setQueue(queue);
        template.convertAndSend(queue, queue, msg);

        connectionFactory.destroy();
    }

}
