package vispDataProvider.datasources;


import org.springframework.data.repository.CrudRepository;
import vispDataProvider.entities.GenerationState;

public interface DataGenerationStateRepository extends CrudRepository<GenerationState, Long> {


}
