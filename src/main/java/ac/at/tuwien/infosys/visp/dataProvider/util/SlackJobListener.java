package ac.at.tuwien.infosys.visp.dataProvider.util;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SlackJobListener extends JobListenerSupport {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private String name;
    private String channel;
    private String token;

    public SlackJobListener(String name, String token, String channel) {
        this.name = name;
        this.token = token;
        this.channel = channel;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(token);
        try {
            session.connect();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        SlackChannel channel = session.findChannelByName(this.channel);
        session.sendMessage(channel, "Data Provider for task: " + context.getJobDetail().getKey() + "--" + context.getJobDetail().getDescription() + "has finished." );
    }
}