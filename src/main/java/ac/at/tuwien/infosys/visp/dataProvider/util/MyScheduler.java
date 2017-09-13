package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.entities.CreateTaskDto;
import ac.at.tuwien.infosys.visp.dataProvider.job.MachineDataProvider;
import ac.at.tuwien.infosys.visp.dataProvider.job.SequentialWaitGeneratorJob;
import ac.at.tuwien.infosys.visp.dataProvider.job.TaxiDataGeneratorJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

import static ac.at.tuwien.infosys.visp.dataProvider.job.DataGeneratorJob.Types;
import static ac.at.tuwien.infosys.visp.dataProvider.util.GenerationPatternsService.Patterns;

@Service
public class MyScheduler {
    @Autowired
    public SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private EndpointConfigurationService ecs;

    public void scheduleJob(CreateTaskDto task) throws SchedulerException {

        Map<String, Object> jobData = new HashMap<>();
        Class jobClass = getJobClass(task.getType());
        JobDetail jobDetail;
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Map<String, Integer> patternProperties = task.getPatternProperties();


        jobData.putAll(task.getPatternProperties());

        jobData.put("pattern", task.getPattern());
        jobData.put("host", ecs.getConfiguration().getHost());
        jobData.put("user", ecs.getConfiguration().getName());
        jobData.put("password", ecs.getConfiguration().getPassword());

        if(task.getCustomPattern() != null){
            int[] values = Arrays.stream(task.getCustomPattern().split(","))
                    .mapToInt(value -> Integer.valueOf(value))
                    .toArray();

            if(values.length > 0)
                jobData.put("customData", values);
        }


        jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(task.getType() + "-" + task.getPattern(), UUID.randomUUID().toString())
                .setJobData(new JobDataMap(jobData))
                .withDescription("")
                .build();

        //withRepeatCount(iterations - 1)
        //iterations - 1 since it will be executed "iterations + 1" times
        //http://www.quartz-scheduler.org/api/2.2.1/org/quartz/SimpleScheduleBuilder.html#withRepeatCount(int)
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMilliseconds(patternProperties.getOrDefault("frequency", 1000))
                .withRepeatCount(patternProperties.getOrDefault("iterations", 1000) - 1);

        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(UUID.randomUUID().toString(), UUID.randomUUID().toString())
                .withSchedule(scheduleBuilder)
                .build();

        if ((ecs.getConfiguration().getSlacktoken() != "") && (ecs.getConfiguration().getSlacktoken() != null)) {
            scheduler.getListenerManager().addSchedulerListener(new SlackSchedulerListener("slacklistener", ecs.getConfiguration().getSlacktoken(), ecs.getConfiguration().getSlackchannel()));
        }

        scheduler.scheduleJob(jobDetail, trigger);

    }

    private Class getJobClass(Types type){
        switch (type){
            case MACHINE_DATA:
                return MachineDataProvider.class;
            case SEQUENTIAL_WAIT_GENERATOR:
                return SequentialWaitGeneratorJob.class;
            case TAXI_DATA_GENERATOR:
                return TaxiDataGeneratorJob.class;
            default:
                throw new IllegalArgumentException("type unknown");
        }
    }
}