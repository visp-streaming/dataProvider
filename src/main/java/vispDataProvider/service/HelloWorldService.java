package vispDataProvider.service;

import entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldService.class);
    private static final String QUEUE_NAME = "helloWorld";
    public void hello() {
        Message msg = new Message("Hello World");

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(QUEUE_NAME);
        template.convertAndSend(msg);
        connectionFactory.destroy();
    }
}
