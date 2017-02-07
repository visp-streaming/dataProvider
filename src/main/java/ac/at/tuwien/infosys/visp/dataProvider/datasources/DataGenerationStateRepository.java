package ac.at.tuwien.infosys.visp.dataProvider.datasources;


import ac.at.tuwien.infosys.visp.dataProvider.entities.GenerationState;
import org.springframework.data.repository.CrudRepository;

public interface DataGenerationStateRepository extends CrudRepository<GenerationState, Long> {


}
