package vispDataProvider.job;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.service.HelloWorldService;

public class HelloWorldJob implements Job {

        @Autowired
        private HelloWorldService service;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            JobDataMap jdMap = jobExecutionContext.getJobDetail().getJobDataMap();
            String lastTime = "111";
            if (jdMap.get("lastTime") != null) {
                lastTime = jdMap.get("lastTime").toString();
            }

            lastTime = service.hello(lastTime);
            jobExecutionContext.getJobDetail().getJobDataMap().put("lastTime", lastTime);
        }
}
