package vispDataProvider.generationPattern;

import vispDataProvider.entities.GenerationState;

public class PatternSelector {

    private String generationPattern;

    public PatternSelector(String pattern) {
        this.generationPattern = pattern;
    }

    public GenerationState iterate(GenerationState state) {
        GenerationPattern pattern = null;

        switch(generationPattern) {
            case "LongSinus": pattern = new LongSinus(); break;
            case "Constant": pattern = new Constant(); break;
            case "LinearIncrease": pattern = new LinearIncrease(); break;
            case "Pyramid": pattern = new Pyramid(); break;
            default: throw new IllegalArgumentException("configured Pattern is not available");
        }

        return pattern.iterate(state);
    }
}
