package vispDataProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class Webtrigger {

    @Autowired
    public MyScheduler myScheduler;

    @RequestMapping(method = RequestMethod.GET)
    public String hitme() throws Exception {

        myScheduler.scheduleJob();

        return "muh";

    }


}
