package engineer.thomas_werner.dashboard.repositories;

import engineer.thomas_werner.dashboard.domain.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SprintRepository extends MongoRepository<Sprint, String> {

    List<Sprint> findByNumber(Integer number);

}
