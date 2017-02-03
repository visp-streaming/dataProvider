package vispDataProvider;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import vispDataProvider.job.PeerJMachineDataProvider;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyScheduler {
    @Autowired
    public SchedulerFactoryBean schedulerFactoryBean;


    //TODO have look at https://gist.github.com/jelies/5085593

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${generationPattern}")
    private String generationPattern;

    @Value("${dataGenerator.frequency}")
    private Integer frequency;

    @Value("${dataGenerator.iterations}")
    private Integer iterations;

    public void scheduleJob() throws SchedulerException {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        Map<String,Object> map = new HashMap<>();
        map.put("pattern", generationPattern);
        map.put("host", RABBITMQ_HOST);
        map.put("user", rabbitmqUsername);
        map.put("password", rabbitmqPassword);


        //TODO get data from

        JobDetail jobDetail = JobBuilder.newJob(PeerJMachineDataProvider.class)
                .withIdentity("job1", "group1")
                .setJobData(new JobDataMap(map))
                .build();

        SimpleScheduleBuilder scheduleBuilder1 = SimpleScheduleBuilder.
                simpleSchedule().
                withIntervalInMilliseconds(frequency)
                .withRepeatCount(iterations);


        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder1).build();


        scheduler.scheduleJob(jobDetail, trigger);

    }

}