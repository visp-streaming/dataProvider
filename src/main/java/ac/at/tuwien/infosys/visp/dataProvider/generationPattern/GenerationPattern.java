package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public interface GenerationPattern {

    GenerationState iterate(GenerationState state);

}
