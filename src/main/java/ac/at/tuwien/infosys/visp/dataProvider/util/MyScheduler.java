package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.job.MachineDataProvider;
import ac.at.tuwien.infosys.visp.dataProvider.job.SequentialWaitGeneratorJob;
import ac.at.tuwien.infosys.visp.dataProvider.job.TaxiDataGeneratorJob;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MyScheduler {
    @Autowired
    public SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private EndpointConfigurationService ecs;

    @Value("${bot}")
    private String slackBot;

    public void scheduleJob(String type, String pattern, Integer frequency, Integer iterations) throws SchedulerException {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        Map<String,Object> map = new HashMap<>();
        map.put("pattern", pattern);
        map.put("host", ecs.getConfiguration().getHost());
        map.put("user", ecs.getConfiguration().getName());
        map.put("password", ecs.getConfiguration().getPassword());

        JobDetail jobDetail = JobBuilder.newJob(SequentialWaitGeneratorJob.class).build();

        if (type.equals("Machine Data")) {
            jobDetail = JobBuilder.newJob(MachineDataProvider.class)
                    .withIdentity(type + "-" + pattern, UUID.randomUUID().toString())
                    .setJobData(new JobDataMap(map))
                    .withDescription("")
                    .build();
        }

        if (type.equals("Sequential Wait")) {
            jobDetail = JobBuilder.newJob(SequentialWaitGeneratorJob.class)
                    .withIdentity(type + "-" + pattern, UUID.randomUUID().toString())
                    .setJobData(new JobDataMap(map))
                    .withDescription("")
                    .build();
        }

        if (type.equals("Taxi Data")) {
            jobDetail = JobBuilder.newJob(TaxiDataGeneratorJob.class)
                    .withIdentity(type + "-" + pattern, UUID.randomUUID().toString())
                    .setJobData(new JobDataMap(map))
                    .withDescription("")
                    .build();
        }


        SimpleScheduleBuilder scheduleBuilder1 = SimpleScheduleBuilder.
                simpleSchedule()
                .withIntervalInMilliseconds(frequency)
                .withRepeatCount(iterations);

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(UUID.randomUUID().toString(), UUID.randomUUID().toString())
                .withSchedule(scheduleBuilder1).build();

        scheduler.getListenerManager().addJobListener(new SlackJobListener("slacklistener", slackBot), KeyMatcher.keyEquals(jobDetail.getKey()));
        scheduler.scheduleJob(jobDetail, trigger);

    }
}