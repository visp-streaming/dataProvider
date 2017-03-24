package ac.at.tuwien.infosys.visp.dataProvider.job;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;
import ac.at.tuwien.infosys.visp.dataProvider.generationPattern.PatternSelector;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class DataGeneratorJob extends QuartzJobBean {

    protected String pattern;
    protected String host;
    protected String user;
    protected String password;

    protected JobDataMap jdMap;

    protected GenerationState state = new GenerationState();

    protected static final Logger LOG = LoggerFactory.getLogger(DataGeneratorJob.class);

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) {

        jdMap = jobExecutionContext.getJobDetail().getJobDataMap();

        if (jdMap.get("amount") != null) {
            state.setAmount(Integer.parseInt(jdMap.get("amount").toString()));
        }

        if (jdMap.get("iteration") != null) {
            state.setIteration(Integer.parseInt(jdMap.get("iteration").toString()));
        }

        if (jdMap.get("direction") != null) {
            state.setDirection(jdMap.get("direction").toString());
        }

        if (jdMap.get("overallCounter") != null) {
            state.setOverallCounter(Integer.parseInt(jdMap.get("overallCounter").toString()));
        }

        customDataGeneration();
        state = new PatternSelector(pattern).iterate(state);
        storeGenerationState();
    }


    protected abstract void customDataGeneration();

    private void storeGenerationState() {
        jdMap.put("direction", state.getDirection());
        jdMap.put("amount", state.getAmount().toString());
        jdMap.put("iteration", state.getIteration().toString());
        jdMap.put("overallCounter", state.getOverallCounter().toString());
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Message createMessage(String type, String payload) {
        MessageProperties props = new MessageProperties();
        props.setHeader("type", type);
        Message message = new Message(payload.getBytes(), props);
        return message;
    }

    public Message createMessage(String type, byte[] payload) {
        MessageProperties props = new MessageProperties();
        props.setHeader("type", type);
        Message message = new Message(payload, props);
        return message;
    }

    public Message createEmptyMessage() {
        MessageProperties props = new MessageProperties();
        props.setHeader("type", "empty");
        Message message = new Message(null, props);
        return message;
    }
}
