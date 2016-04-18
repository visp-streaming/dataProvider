package vispDataProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import vispDataProvider.utilities.RabbitMQCleanup;

import static java.util.Arrays.asList;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RabbitMQCleanup rabbitMQCleanup;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        rabbitMQCleanup.cleanupRabbitMQQueues(asList("aggregationsource", "analysisavgSpeed", "analysisdistance", "avgSpeedspeed", "distanceaggregation", "monitoranalysis", "monitorsource", "speedsource"));

    }
}
