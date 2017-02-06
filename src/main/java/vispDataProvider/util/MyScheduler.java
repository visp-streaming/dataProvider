package vispDataProvider.util;

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

        //TODO encode name/pattern somehow


        //TODO get data from

        JobDetail jobDetail = JobBuilder.newJob(PeerJMachineDataProvider.class)
                .withIdentity("job1", "group1")
                .setJobData(new JobDataMap(map))
                .withDescription("blabla")
                .build();

        SimpleScheduleBuilder scheduleBuilder1 = SimpleScheduleBuilder.
                simpleSchedule()
                .withIntervalInMinutes(1)
                .withRepeatCount(iterations);

        //withIntervalInMilliseconds(frequency)


        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder1).build();


        scheduler.scheduleJob(jobDetail, trigger);

    }

}