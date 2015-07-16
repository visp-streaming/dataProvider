package vispDataProvider.job;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.service.HelloWorldService;

public class HelloWorldJob implements Job {

        @Autowired
        private HelloWorldService service;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            service.hello();
        }
}
