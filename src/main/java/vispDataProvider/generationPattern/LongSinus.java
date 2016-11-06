package vispDataProvider.generationPattern;


import vispDataProvider.entities.GenerationState;

public class LongSinus implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        if (state.getDirection().equals("up")) {
            if (state.getIteration()>30) {
                if (state.getAmount()<10) {
                    state.setAmount((state.getAmount() + 1));
                    state.setIteration(1);
                } else {
                    state.setAmount((state.getAmount() - 1));
                    state.setIteration(1);
                    state.setDirection("down");
                }
            } else {
                state.setIteration((state.getIteration() + 1));
            }
        }

        if (state.getDirection().equals("down")) {
            if (state.getIteration()>30) {
                if (state.getAmount()>2) {
                    state.setAmount((state.getAmount() - 1));
                    state.setIteration(1);
                } else {
                    state.setAmount((state.getAmount() + 1));
                    state.setIteration(1);
                    state.setDirection("up");
                }
            } else {
                state.setIteration((state.getIteration() + 1));
            }
        }
        state.setOverallCounter(state.getOverallCounter() + 1);

        return state;
    }
}
