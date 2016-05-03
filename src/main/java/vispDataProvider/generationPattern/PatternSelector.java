package vispDataProvider.generationPattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vispDataProvider.entities.GenerationState;

@Service
public class PatternSelector {

    @Value("${generationPattern}")
    private String generationPattern;

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
