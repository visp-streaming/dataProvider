package vispDataProvider.service;

import entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService1 implements HelloWorldService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldService1.class);

    @Value("${rabbitMQ.outgoingQueue}")
    private String QUEUE_NAME;

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    public void hello(Message msg) {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(QUEUE_NAME);
        template.setQueue(QUEUE_NAME);
        template.convertAndSend(QUEUE_NAME, QUEUE_NAME, msg);

        connectionFactory.destroy();
    }

}
