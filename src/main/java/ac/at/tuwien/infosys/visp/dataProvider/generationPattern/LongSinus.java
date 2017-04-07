package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class LongSinus implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        if (state.getDirection().equals("up")) {
            if (state.getIteration()>30) {
                if (state.getAmount()<10) {
                    state.increaseAmount();
                    state.setIteration(1);
                } else {
                    state.decreaseAmount();
                    state.setIteration(1);
                    state.setDirection("down");
                }
            } else {
                state.increaseIteration();
            }
        }

        if (state.getDirection().equals("down")) {
            if (state.getIteration()>30) {
                if (state.getAmount()>2) {
                    state.decreaseAmount();
                    state.setIteration(1);
                } else {
                    state.increaseAmount();
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
