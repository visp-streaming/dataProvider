package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;

import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

import java.util.HashSet;
import java.util.Set;

public class ChangeAndHoldPattern extends GenerationPattern {

    @Override
    public GenerationState iterate(GenerationState state) {

        Integer changeIterations = state.getAdditionalProperty("change");
        Integer holdIterations = state.getAdditionalProperty("hold");
        Integer maxMessage = state.getAdditionalProperty("maxMessagesPerIteration");
        Integer minMessage = state.getAdditionalProperty("minMessagesPerIteration");

        Integer currentIteration = new Integer(state.getIteration());
        //start iterations by 0, since it is easier to calculate y = k * y + d
        currentIteration--;
        Integer amount = 0;

        //increase amount
        if(currentIteration <= changeIterations){
            float tmp = (float)((maxMessage - minMessage) / changeIterations) * currentIteration + minMessage;
            amount = Math.round(tmp);
        }
        //keep maximum amount
        else if (currentIteration > changeIterations
                && currentIteration <= (holdIterations + changeIterations)){

            amount = maxMessage;
        }
        //decrease amount
        else if (currentIteration > (holdIterations + changeIterations)
                && currentIteration <= (holdIterations + (changeIterations * 2))) {
            float tmp = (float)((minMessage - maxMessage) / changeIterations) * (currentIteration - changeIterations - holdIterations) + maxMessage;
            amount = Math.round(tmp);
        }
        //keep minimum (one iteration less then actually necessary)
        else if (currentIteration > (2 * changeIterations) + holdIterations
                && currentIteration < (2 * changeIterations) +  (2 * holdIterations)){
            amount = minMessage;
        }
        //last "minimum" iteration and set iteration counter will get reset to 0
        else {
            amount = minMessage;
            state.setIteration(0);
        }

        state.setActualAmount(amount);
        state.increaseIteration();
        state.increaseOverallCounter();
        return state;
    }

    @Override
    public Set<String> getPropertyNames() {
        Set<String> propertyNames = new HashSet<>();

        propertyNames.add("frequency");
        propertyNames.add("iterations");
        propertyNames.add("change");
        propertyNames.add("hold");
        propertyNames.add("minMessagesPerIteration");
        propertyNames.add("maxMessagesPerIteration");


        return propertyNames;
    }
}
