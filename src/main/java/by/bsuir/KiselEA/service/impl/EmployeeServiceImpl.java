package by.bsuir.KiselEA.service.impl;

import by.bsuir.KiselEA.entity.Employee;
import by.bsuir.KiselEA.exception.EmployeeNotExistException;
import by.bsuir.KiselEA.repository.EmployeeRepository;
import by.bsuir.KiselEA.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        readLock = reentrantReadWriteLock.readLock();
        writeLock = reentrantReadWriteLock.writeLock();
    }

    @Override
    public Collection<Employee> getEmployees() {
        try {
            readLock.lock();
            return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Employee findEmployeeById(Long id) {
        try {
            readLock.lock();
            Optional<Employee> optionalPriority = employeeRepository.findById(id);
            if (optionalPriority.isPresent()) {
                return optionalPriority.get();
            } else {
                throw new EmployeeNotExistException();
            }
        } finally {
            readLock.unlock();
        }
    }
}
