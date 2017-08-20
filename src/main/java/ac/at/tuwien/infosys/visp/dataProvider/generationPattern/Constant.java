package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class Constant extends GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        state.setAmount(5);
        state.setIteration(state.getIteration()+1);
        state.increaseOverallCounter();

        return state;
    }
}
