package by.bsuir.KiselEA.service;


import by.bsuir.KiselEA.entity.Employee;

import java.util.Collection;

public interface EmployeeService {

    Collection<Employee> getEmployees();

    Employee findEmployeeById(Long id);
}
