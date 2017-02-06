package vispDataProvider.ui;


import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vispDataProvider.util.JobUtility;
import vispDataProvider.util.MyScheduler;

@Controller
public class UtilController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MyScheduler myScheduler;

    @Autowired
    private JobUtility jobUtility;

    @RequestMapping("/")
    public String index(Model model) throws SchedulerException {

        model.addAttribute("message", null);
        model.addAttribute("tasks", jobUtility.getTasks());
        return "tasks";
    }

    @RequestMapping("/newTask")
    public String run(Model model) {

        //TODO list all currently running tasks and their progress and provide a kill app

        model.addAttribute("message", null);
        //model.addAttribute("services", isr.findAll());
        return "about";
    }

    @RequestMapping("/configuration")
    public String configuration(Model model) {

        //TODO list all currently running tasks and their progress and provide a kill app

        model.addAttribute("message", null);
        //model.addAttribute("services", isr.findAll());
        return "about";
    }

    @RequestMapping("/runTask")
    public String runTask(Model model) throws SchedulerException {

        myScheduler.scheduleJob();


        //TODO list all currently running tasks and their progress and provide a kill app

        model.addAttribute("message", null);
        model.addAttribute("tasks", jobUtility.getTasks());
        return "tasks";
    }


    @RequestMapping("/killtask/{group}/{name}")
    public String utilKillSingle(Model model, @PathVariable String group, @PathVariable String name) throws SchedulerException {

        jobUtility.deleteTask(name, group);

        model.addAttribute("message", "Task in the group \"" + group + "\" with the name \"" + name + "\" has been killed.");
        model.addAttribute("tasks", jobUtility.getTasks());
        return "tasks";
    }


}
