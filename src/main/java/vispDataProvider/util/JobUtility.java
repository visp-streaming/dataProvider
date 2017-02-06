package vispDataProvider.util;


import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import vispDataProvider.entities.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class JobUtility {

    @Autowired
    public SchedulerFactoryBean schedulerFactoryBean;

    public List<Task> getTasks() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<Task> taskList = new ArrayList<>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {

                Task task = new Task();

                task.setJobGroup(jobKey.getGroup());
                task.setJobName(jobKey.getName());

                if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                    task.setDescription(jobDetail.getDescription());

                    if (simpleTrigger.getRepeatCount()<1) {
                        task.setProgress(0.0);
                    } else {
                        if (jobDetail.getJobDataMap().get("overallCounter")!= null) {
                            Double progress = Double.parseDouble(jobDetail.getJobDataMap().get("overallCounter").toString());
                            task.setProgress((progress/simpleTrigger.getRepeatCount() * 100));
                        } else {
                            task.setProgress(0.0);
                        }
                    }
                }
                taskList.add(task);
            }
        }
        return taskList;
    }


    public void deleteTask(String job, String group) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(group);
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            if (jobKey.getName().equals(job)) {
                scheduler.deleteJob(jobKey);
            }
        }
    }


}
