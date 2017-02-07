package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class Constant implements GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        state.setAmount(5);
        state.setIteration(state.getIteration()+1);
        state.setOverallCounter(state.getOverallCounter() + 1);

        return state;
    }
}
