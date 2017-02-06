package vispDataProvider.generationPattern;


import vispDataProvider.entities.GenerationState;

public interface GenerationPattern {

    GenerationState iterate(GenerationState state);

}
