package vispDataProvider.generationPattern;


import vispDataProvider.entities.GenerationState;

public class LinearIncrease implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        if ((state.getIteration() % 5) == 0) {
            //increase the load every 5 iterations
            state.setAmount(state.getAmount()+1);
        }


        state.setIteration(state.getIteration()+1);
        state.setOverallCounter(state.getOverallCounter() + 1);

        return state;
    }
}
