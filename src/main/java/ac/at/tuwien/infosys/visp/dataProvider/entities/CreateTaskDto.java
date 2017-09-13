package ac.at.tuwien.infosys.visp.dataProvider.entities;

import ac.at.tuwien.infosys.visp.dataProvider.job.DataGeneratorJob;
import ac.at.tuwien.infosys.visp.dataProvider.util.GenerationPatternsService;

import java.util.Map;

public class CreateTaskDto {

    private GenerationPatternsService.Patterns pattern;
    private DataGeneratorJob.Types type;
    private String customPattern;
    private Map<String, Integer> patternProperties;

    public GenerationPatternsService.Patterns getPattern() {
        return pattern;
    }

    public void setPattern(GenerationPatternsService.Patterns pattern) {
        this.pattern = pattern;
    }

    public DataGeneratorJob.Types getType() {
        return type;
    }

    public void setType(DataGeneratorJob.Types type) {
        this.type = type;
    }

    public Map<String, Integer> getPatternProperties() {
        return patternProperties;
    }

    public void setPatternProperties(Map<String, Integer> patternProperties) {
        this.patternProperties = patternProperties;
    }

    public String getCustomPattern() {
        return customPattern;
    }

    public void setCustomPattern(String customPattern) {
        this.customPattern = customPattern;
    }
}
