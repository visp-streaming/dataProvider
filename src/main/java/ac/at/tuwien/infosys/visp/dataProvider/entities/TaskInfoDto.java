package ac.at.tuwien.infosys.visp.dataProvider.entities;

import ac.at.tuwien.infosys.visp.dataProvider.job.DataGeneratorJob;
import ac.at.tuwien.infosys.visp.dataProvider.util.GenerationPatternsService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskInfoDto {

    private List<DataGeneratorJob.Types> generators;
    private Map<GenerationPatternsService.Patterns, Set<String>> patterns;

    public List<DataGeneratorJob.Types> getGenerators() {
        return generators;
    }

    public void setGenerators(List<DataGeneratorJob.Types> generators) {
        this.generators = generators;
    }

    public Map<GenerationPatternsService.Patterns, Set<String>> getPatterns() {
        return patterns;
    }

    public void setPatterns(Map<GenerationPatternsService.Patterns, Set<String>> patterns) {
        this.patterns = patterns;
    }
}
