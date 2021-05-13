package by.bsuir.KiselEA.service.impl;

import by.bsuir.KiselEA.entity.Project;
import by.bsuir.KiselEA.exception.ProjectNotExistException;
import by.bsuir.KiselEA.repository.ProjectRepo;
import by.bsuir.KiselEA.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepo projectRepo;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public ProjectServiceImpl(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        readLock = reentrantReadWriteLock.readLock();
        writeLock = reentrantReadWriteLock.writeLock();
    }

    @Override
    public Collection<Project> getProjects() {
        try {
            readLock.lock();
            Iterable<Project> all = projectRepo.findAll();
            if (!all.iterator().hasNext()) {
                return null;
            }
            return StreamSupport.stream(all.spliterator(), false)
                    .collect(Collectors.toList());

        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Project findById(long id) {
        try {
            readLock.lock();
            Optional<Project> optional = projectRepo.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new ProjectNotExistException();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void saveProject(Project project) {
        try {
            writeLock.lock();
            projectRepo.save(project);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void updateProject(Project project) {
        try {
            writeLock.lock();
            if (!projectRepo.existsById(project.getId())) {
                throw new ProjectNotExistException();
            }
            projectRepo.save(project);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void deleteProject(long id) {
        try {
            writeLock.lock();
            if (projectRepo.existsById(id)) {
                projectRepo.deleteById(id);
            } else {
                throw new ProjectNotExistException();
            }

        } finally {
            writeLock.unlock();
        }
    }
}
