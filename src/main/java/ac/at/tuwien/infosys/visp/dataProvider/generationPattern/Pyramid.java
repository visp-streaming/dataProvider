package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class Pyramid implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        if ((state.getIteration() % 5) == 0) {
            //increase the load every 5 iterations until 200 iterations and then decrease it again
            if (state.getIteration() < 200) {
                state.setAmount(state.getAmount() + 1);
            } else {
                state.setAmount(state.getAmount() - 1);
            }

            if (state.getAmount()<1) {
                state.setAmount(0);
            }

        }
        
        state.setIteration(state.getIteration()+1);
        state.setOverallCounter(state.getOverallCounter() + 1);

        return state;
    }
}
