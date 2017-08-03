package ac.at.tuwien.infosys.visp.dataProvider.ui;


import ac.at.tuwien.infosys.visp.dataProvider.entities.CreateTaskForm;
import ac.at.tuwien.infosys.visp.dataProvider.entities.EndpointConfiguration;
import ac.at.tuwien.infosys.visp.dataProvider.util.EndpointConfigurationService;
import ac.at.tuwien.infosys.visp.dataProvider.util.JobUtility;
import ac.at.tuwien.infosys.visp.dataProvider.util.MyScheduler;
import ac.at.tuwien.infosys.visp.dataProvider.util.Presets;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UtilController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MyScheduler myScheduler;

    @Autowired
    private JobUtility jobUtility;

    @Autowired
    private Presets presets;

    @Autowired
    private EndpointConfigurationService ecs;

    @RequestMapping("/")
    public String index(@CookieValue(value = "host", defaultValue = "") String host, @CookieValue(value = "host", defaultValue = "") String slacktoken, Model model) throws SchedulerException {

        ecs.storeFromCoockies(host, slacktoken);

        if (ecs.getConfiguration().getHost() == null) {
            model.addAttribute("message", "The receiving endpoint needs to be configured.");
        } else {
            model.addAttribute("message", null);
        }

        model.addAttribute("tasks", jobUtility.getTasks());
        return "tasks";
    }

    @RequestMapping("/newTask")
    public String run(Model model) {

        CreateTaskForm taskform = new CreateTaskForm();
        taskform.setFrequency(480);
        taskform.setIterations(20000);

        model.addAttribute("types", presets.getTypes());
        model.addAttribute("patterns", presets.getPatterns());
        model.addAttribute(taskform);

        if (ecs.getConfiguration().getHost() == null) {
            model.addAttribute("message", "The receiving endpoint needs to be configured.");
        } else {
            model.addAttribute("message", null);
        }

        return "createTask";
    }

    @RequestMapping(value="/createTask", method= RequestMethod.POST)
    public String userCreated(@ModelAttribute CreateTaskForm form, Model model) throws SchedulerException {

        myScheduler.scheduleJob(form.getType(), form.getPattern(), form.getFrequency(), form.getIterations());

        model.addAttribute("message", "A new task has beeen started.");
        model.addAttribute("tasks", jobUtility.getTasks());

        return "tasks";
    }


    @RequestMapping("/configuration")
    public String configuration(@CookieValue(value = "host", defaultValue = "") String host, @CookieValue(value = "host", defaultValue = "") String slacktoken, Model model) {

        ecs.storeFromCoockies(host, slacktoken);

        model.addAttribute("endpointConfiguration", ecs.getConfiguration());
        model.addAttribute("message", null);

        return "configuration";
    }

    @RequestMapping(value="/configurationSaved", method= RequestMethod.POST)
    public String configurationSaved(@ModelAttribute EndpointConfiguration form, Model model, HttpServletResponse response) throws SchedulerException {

        ecs.storeData(form);


        model.addAttribute("endpointConfiguration", ecs.getConfiguration());
        model.addAttribute("message", "The configuration has been updated.");

        Cookie cookie1 = new Cookie("host", ecs.getConfiguration().getHost());
        cookie1.setMaxAge(265 * 24 * 60 * 60);  // (s)
        cookie1.setPath("/");

        Cookie cookie2 = new Cookie("slacktoken", ecs.getConfiguration().getSlacktoken());
        cookie2.setMaxAge(265 * 24 * 60 * 60);  // (s)
        cookie2.setPath("/");

        response.addCookie(cookie1);
        response.addCookie(cookie2);

        return "configuration";
    }


    @RequestMapping("/killtask/{group}/{name}")
    public String utilKillSingle(Model model, @PathVariable String group, @PathVariable String name) throws SchedulerException {

        jobUtility.deleteTask(name, group);

        model.addAttribute("message", "Task in the group \"" + group + "\" with the name \"" + name + "\" has been killed.");
        model.addAttribute("tasks", jobUtility.getTasks());
        return "tasks";
    }
}
