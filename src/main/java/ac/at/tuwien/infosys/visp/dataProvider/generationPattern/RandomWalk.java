package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import java.util.HashMap;

import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class RandomWalk extends GenerationPattern{

    public GenerationState iterate(GenerationState state) {

        //todo use three time as interval to have better variability of the machines (1-9) instead of 1-3

        //map needs to be numberes by 1 - n
        HashMap<Integer, Integer> states = new HashMap<>();
        states.put(1, 1);
        states.put(2, 2);
        states.put(3, 1);
        states.put(4, 2);
        states.put(5, 3);

        //direction has encoded the state within the hashmap

        state.increaseOverallCounter();

        if (state.getIteration() > 100) {
            Integer currentlocation = Integer.parseInt(state.getDirection());
            currentlocation = currentlocation + 1;

            if ((currentlocation - 1) > states.size()) {
                currentlocation = 1;
            }

            state.setAmount(states.get(currentlocation));
            state.setDirection(currentlocation.toString());
            state.setIteration(1);
            return state;
        }


        state.increaseIteration();
        return state;
    }
}
