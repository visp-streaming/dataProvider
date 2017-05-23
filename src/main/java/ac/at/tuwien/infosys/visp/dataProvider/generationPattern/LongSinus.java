package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class LongSinus implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        state.increaseOverallCounter();

        if (state.getDirection().equals("up")) {
            if (state.getIteration()>500) {
                if (state.getAmount()<3) {
                    state.increaseAmount();
                    state.setIteration(1);
                    return state;
                } else {
                    state.setDirection("down");
                    state.decreaseAmount();
                    state.setIteration(1);
                    return state;
                }
            }
        }

        if (state.getDirection().equals("down")) {
            if (state.getIteration()>500) {
                if (state.getAmount()>1) {
                    state.decreaseAmount();
                    state.setIteration(1);
                    return state;
                } else {
                    state.setDirection("up");
                    state.increaseAmount();
                    state.setIteration(1);
                    return state;
                }
            }
        }

        state.increaseIteration();
        return state;
    }
}
