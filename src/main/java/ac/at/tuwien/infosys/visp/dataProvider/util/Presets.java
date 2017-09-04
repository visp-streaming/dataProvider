package ac.at.tuwien.infosys.visp.dataProvider.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

@Controller
public class Presets {

    public List<String> getTypes() {
        List<String> types = new ArrayList<>();
        types.add("Machine Data");
        types.add("Sequential Wait");
        types.add("Taxi Data");
        return types;
    }

    public List<String> getPatterns() {
        List<String> patterns = new ArrayList<>();
        patterns.add("Constant");
        patterns.add("Linear Increase");
        patterns.add("Long Sinus");
        patterns.add("Sinus");
        patterns.add("Pyramid");
        patterns.add("Random Walk");
        patterns.add("Random Walk Half");
        patterns.add("Random Walk 2");
        patterns.add("Random Walk Half 2");
        return patterns;
    }

}
