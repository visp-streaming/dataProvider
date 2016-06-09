package vispDataProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import vispDataProvider.utilities.RabbitMQCleanup;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RabbitMQCleanup rabbitMQCleanup;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        //rabbitMQCleanup.cleanupRabbitMQQueues(asList("aggregationsource", "analysisavgSpeed", "analysisdistance", "avgSpeedspeed", "distanceaggregation", "monitoranalysis", "monitorsource", "speedsource"));

        //rabbitMQCleanup.cleanupRabbitMQQueues(asList("speedsource", "step1source", "step2step1", "step3step2", "step4step3", "step5step4", "logstep5", "logstep"));



    }
}
