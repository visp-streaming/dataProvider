package vispDataProvider.job;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.datasources.DataGenerationStateRepository;
import vispDataProvider.entities.GenerationState;
import vispDataProvider.generationPattern.PatternSelector;

@PersistJobDataAfterExecution
public abstract class DataGeneratorJob implements Job {

    @Autowired
    protected DataGenerationStateRepository logger;

    @Autowired
    protected PatternSelector generationPattern;

    protected JobDataMap jdMap;

    protected GenerationState state = new GenerationState();

    protected static final Logger LOG = LoggerFactory.getLogger(DataGeneratorJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

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

        customDataGeneration();

        state = generationPattern.iterate(state);
        storeGenerationState();
    }


    protected abstract void customDataGeneration();

    private void storeGenerationState() {
        jdMap.put("direction", state.getDirection());
        jdMap.put("amount", state.getAmount().toString());
        jdMap.put("iteration", state.getIteration().toString());
    }


}
