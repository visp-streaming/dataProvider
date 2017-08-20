package ac.at.tuwien.infosys.visp.dataProvider.ui;

import ac.at.tuwien.infosys.visp.dataProvider.entities.CreateTaskDto;
import ac.at.tuwien.infosys.visp.dataProvider.entities.CreateTaskForm;
import ac.at.tuwien.infosys.visp.dataProvider.entities.EndpointConfiguration;
import ac.at.tuwien.infosys.visp.dataProvider.entities.TaskInfoDto;
import ac.at.tuwien.infosys.visp.dataProvider.util.EndpointConfigurationService;
import ac.at.tuwien.infosys.visp.dataProvider.util.JobUtility;
import ac.at.tuwien.infosys.visp.dataProvider.util.MyScheduler;
import ac.at.tuwien.infosys.visp.dataProvider.util.PresetGenerator;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private PresetGenerator presetGenerator;

    @Autowired
    private EndpointConfigurationService ecs;

    @RequestMapping(value={"", "/", "tasks"})
    public String index(@CookieValue(value = "host", defaultValue = "") String host, @CookieValue(value = "slacktoken", defaultValue = "") String slacktoken, Model model) throws SchedulerException {

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

        model.addAttribute(taskform);

        if (ecs.getConfiguration().getHost() == null) {
            model.addAttribute("message", "The receiving endpoint needs to be configured.");
        } else {
            model.addAttribute("message", null);
        }

        return "createTask";
    }


    @RequestMapping("/configuration")
    public String configuration(@CookieValue(value = "host", defaultValue = "") String host, @CookieValue(value = "slacktoken", defaultValue = "") String slacktoken, Model model) {

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

    @CrossOrigin
    @RequestMapping(value = "/taskinfos", method = RequestMethod.GET)
    public @ResponseBody TaskInfoDto getTaskInfos(){
        return presetGenerator.getTaskInfos();
    }

    @CrossOrigin
    @RequestMapping(value = "/createTask", method = RequestMethod.POST)
    public ResponseEntity newTask(@RequestBody CreateTaskDto task) throws SchedulerException {
        myScheduler.scheduleJob(task.getType(), task.getPattern(), task.getPatternProperties());
        return new ResponseEntity(HttpStatus.OK);
    }
}
