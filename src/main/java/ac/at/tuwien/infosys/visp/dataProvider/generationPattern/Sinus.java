package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class Sinus implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        state.increaseOverallCounter();

        if (state.getDirection().equals("up")) {
            if (state.getIteration()>1000) {
                    state.setDirection("down");
                    state.setAmount(3);
                    state.setIteration(1);
                    return state;
            }
        }

        if (state.getDirection().equals("down")) {
            if (state.getIteration()>1000) {
                state.setDirection("up");
                state.setAmount(1);
                state.setIteration(1);
                return state;
            }
        }

        state.increaseIteration();
        return state;
    }
}
