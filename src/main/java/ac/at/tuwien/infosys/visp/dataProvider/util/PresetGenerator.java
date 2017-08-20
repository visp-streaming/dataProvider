package ac.at.tuwien.infosys.visp.dataProvider.util;

import ac.at.tuwien.infosys.visp.dataProvider.entities.TaskInfoDto;
import ac.at.tuwien.infosys.visp.dataProvider.job.DataGeneratorJob;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PresetGenerator {


    public TaskInfoDto getTaskInfos(){
        TaskInfoDto info = new TaskInfoDto();
        Map<GenerationPatternsService.Patterns, Set<String>> patterns = new TreeMap<>();

        Arrays.stream(GenerationPatternsService.Patterns.values()).forEach(pattern -> {
            patterns.put(pattern, GenerationPatternsService.getInstance(pattern).getPropertyNames());
        });

        info.setPatterns(patterns);

        List<DataGeneratorJob.Types> generators = Arrays.asList(DataGeneratorJob.Types.values());
        Collections.sort(generators);
        info.setGenerators(generators);

        return info;
    }
}
