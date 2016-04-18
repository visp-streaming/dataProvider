package vispDataProvider.generationPattern;


import vispDataProvider.entities.GenerationState;

public interface GenerationPattern {

    public GenerationState iterate(GenerationState state);

}
