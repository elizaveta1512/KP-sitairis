package by.bsuir.KiselEA.repository;

import by.bsuir.KiselEA.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}
