package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;

import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

import java.util.HashSet;
import java.util.Set;

public class CustomPattern extends GenerationPattern {

    @Override
    public GenerationState iterate(GenerationState state) {

        Integer currentIteration = state.getIteration() - 1;

        if(currentIteration == state.getCustomData().length){
            state.setIteration(1);
            currentIteration = 0;
        }

        Integer amount = state.getCustomData()[currentIteration];
        System.out.println(amount);

        state.increaseIteration();
        state.increaseOverallCounter();

        return state;
    }

    @Override
    public Set<String> getPropertyNames() {
        Set<String> propertyNames = new HashSet<>();

        propertyNames.add("frequency");
        propertyNames.add("iterations");
        propertyNames.add("customPattern");

        return propertyNames;
    }
}
