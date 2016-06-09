package vispDataProvider.generationPattern;


import vispDataProvider.entities.GenerationState;

public class Constant implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        state.setAmount(5);
        state.setIteration(state.getIteration()+1);

        return state;
    }
}
