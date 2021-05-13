package by.bsuir.KiselEA.repository;

import by.bsuir.KiselEA.entity.Sprint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepo extends CrudRepository<Sprint, Long> {

}
