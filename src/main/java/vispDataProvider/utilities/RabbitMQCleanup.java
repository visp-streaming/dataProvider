package vispDataProvider.utilities;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQCleanup {

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    public void cleanupRabbitMQQueues(List<String> queues) {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST);
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);

        for (String queue : queues) {
            admin.purgeQueue(queue, true);
        }

        connectionFactory.destroy();

    }
}
