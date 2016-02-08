package vispDataProvider;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${rabbitMQ.outgoingQueue}")
    private String QUEUE_NAME;

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST);
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);

        admin.purgeQueue("aggregationsource", true);
        admin.purgeQueue("analysisavgSpeed", true);
        admin.purgeQueue("analysisdistance", true);
        admin.purgeQueue("avgSpeedspeed", true);
        admin.purgeQueue("distanceaggregation", true);
        admin.purgeQueue("monitoranalysis", true);
        admin.purgeQueue("monitorsource", true);
        admin.purgeQueue("speedsource", true);


        return;
    }

} // class ApplicationStartup
