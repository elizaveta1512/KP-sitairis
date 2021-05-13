package by.bsuir.KiselEA.service.impl;

import by.bsuir.KiselEA.entity.Priority;
import by.bsuir.KiselEA.exception.PriorityNotExistException;
import by.bsuir.KiselEA.repository.PriorityRepository;
import by.bsuir.KiselEA.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PriorityServiceImpl implements PriorityService {
    private final PriorityRepository priorityRepository;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public PriorityServiceImpl(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        readLock = reentrantReadWriteLock.readLock();
        writeLock = reentrantReadWriteLock.writeLock();
    }

    @Override
    public Collection<Priority> getPriorities() {
        try {
            readLock.lock();
            return StreamSupport.stream(priorityRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Priority findPriorityById(Long id) {
        try {
            readLock.lock();
            Optional<Priority> optionalPriority = priorityRepository.findById(id);
            if (optionalPriority.isPresent()) {
                return optionalPriority.get();
            } else {
                throw new PriorityNotExistException();
            }
        } finally {
            readLock.unlock();
        }
    }
}
