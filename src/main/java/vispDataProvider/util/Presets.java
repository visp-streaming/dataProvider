package vispDataProvider.util;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

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
        patterns.add("Pyramid");
        return patterns;
    }

}
