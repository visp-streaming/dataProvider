package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

import java.util.HashSet;
import java.util.Set;

public abstract class GenerationPattern {

    public abstract GenerationState iterate(GenerationState state);

    public Set<String> getPropertyNames(){
        Set<String> propertyNames = new HashSet<>();

        propertyNames.add("frequency");
        propertyNames.add("iterations");

        return propertyNames;
    }

}
