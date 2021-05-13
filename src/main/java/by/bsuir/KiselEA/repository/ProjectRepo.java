package by.bsuir.KiselEA.repository;

import by.bsuir.KiselEA.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {

}
