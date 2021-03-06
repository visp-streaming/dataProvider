package ac.at.tuwien.infosys.visp.dataProvider.generationPattern;

import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;

public class PatternSelector {

    private String generationPattern;

    public PatternSelector(String pattern) {
        this.generationPattern = pattern;
    }

    public GenerationState iterate(GenerationState state) {
        GenerationPattern pattern = null;

        switch(generationPattern) {
            case "Sinus": pattern = new Sinus(); break;
            case "Long Sinus": pattern = new LongSinus(); break;
            case "Constant": pattern = new Constant(); break;
            case "Linear Increase": pattern = new LinearIncrease(); break;
            case "Pyramid": pattern = new Pyramid(); break;
            case "Random Walk": pattern = new RandomWalk(); break;
            case "Random Walk Half": pattern = new RandomWalkHalf(); break;
            case "Random Walk 2": pattern = new RandomWalk2(); break;
            case "Random Walk Half 2": pattern = new RandomWalkHalf2(); break;

            default: throw new IllegalArgumentException("configured Pattern is not available");
        }

        return pattern.iterate(state);
    }
}
