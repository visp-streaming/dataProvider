package ac.at.tuwien.infosys.visp.dataProvider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SpringBootApplication
public class StreamingDataProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamingDataProviderApplication.class, args);
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();

        return scheduler;
    }

}
