package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.generationPattern.*;

import java.util.HashMap;
import java.util.Map;


public class GenerationPatternsService {

    public enum Patterns {
        CONSTANT, LINEAR_INCREASE, LONG_SINUS, PYRAMID, SINUS, CHANGE_AND_HOLD, RANDOM_WALK
    }

    private static Map<Patterns, GenerationPattern> patterns;
    private static final GenerationPattern defaultPattern = new Constant();

    public static GenerationPattern getInstance(Patterns generationPattern){
        //TODO ask Christoph if "Constant" is allows as default pattern
        return patterns.getOrDefault(generationPattern, defaultPattern);
    }


    static {
        patterns = new HashMap<>();
        patterns.put(Patterns.CONSTANT, new Constant());
        patterns.put(Patterns.LINEAR_INCREASE, new LinearIncrease());
        patterns.put(Patterns.LONG_SINUS, new LongSinus());
        patterns.put(Patterns.PYRAMID, new Pyramid());
        patterns.put(Patterns.SINUS, new Sinus());
        patterns.put(Patterns.CHANGE_AND_HOLD, new ChangeAndHoldPattern());
        patterns.put(Patterns.RANDOM_WALK, new RandomWalk());
    }

}
