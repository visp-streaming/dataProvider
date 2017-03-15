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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String index(Model model) throws SchedulerException {

        if (ecs.getConfiguration().getUri() == null) {
            model.addAttribute("message", "The URI of the receiving endpoint needs to be configured.");
        } else {
            model.addAttribute("message", null);
        }

        model.addAttribute("tasks", jobUtility.getTasks());
        return "tasks";
    }

    @RequestMapping("/newTask")
    public String run(Model model) {

        CreateTaskForm taskform = new CreateTaskForm();
        taskform.setFrequency(500);
        taskform.setIterations(10000);

        model.addAttribute("types", presets.getTypes());
        model.addAttribute("patterns", presets.getPatterns());
        model.addAttribute(taskform);

        if (ecs.getConfiguration().getUri() == null) {
            model.addAttribute("message", "The URI of the receiving endpoint needs to be configured.");
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
    public String configuration(Model model) {

        model.addAttribute("endpointConfiguration", ecs.getConfiguration());
        model.addAttribute("message", null);
        return "configuration";
    }

    @RequestMapping(value="/configurationSaved", method= RequestMethod.POST)
    public String configurationSaved(@ModelAttribute EndpointConfiguration form, Model model) throws SchedulerException {

        ecs.storeData(form);


        model.addAttribute("endpointConfiguration", ecs.getConfiguration());
        model.addAttribute("message", "The configuration has been updated.");
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
