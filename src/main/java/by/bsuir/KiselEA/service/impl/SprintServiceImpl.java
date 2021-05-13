package by.bsuir.KiselEA.service.impl;

import by.bsuir.KiselEA.repository.SprintRepo;
import by.bsuir.KiselEA.entity.Project;
import by.bsuir.KiselEA.entity.Sprint;
import by.bsuir.KiselEA.exception.ProjectNotExistException;
import by.bsuir.KiselEA.exception.SprintNotExistException;
import by.bsuir.KiselEA.repository.ProjectRepo;
import by.bsuir.KiselEA.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SprintServiceImpl implements SprintService {
    private final SprintRepo repo;
    private final ProjectRepo projectRepo;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public SprintServiceImpl(SprintRepo repo, ProjectRepo projectRepo) {
        this.repo = repo;
        this.projectRepo = projectRepo;
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        readLock = reentrantReadWriteLock.readLock();
        writeLock = reentrantReadWriteLock.writeLock();
    }

    @Override
    public Collection<Sprint> getSprintsSortBy(long id) {
        try {
            readLock.lock();
            Collection<Sprint> sprints = StreamSupport.stream(repo.findAll().spliterator(), false)
                    .filter(o -> {
                        Project p = o.getProject();
                        return p != null && p.getId() == id;
                    })
                    .sorted(Comparator.comparing(Sprint::getIsDone))
                    .collect(Collectors.toList());

            if (sprints.isEmpty()) return null;
            return sprints;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Sprint findById(long id) {
        try {
            readLock.lock();
            Optional<Sprint> optional = repo.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new SprintNotExistException();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void saveSprint(Sprint sprint, long projectId) {
        try {
            writeLock.lock();
            if (!projectRepo.existsById(projectId)) {
                throw new ProjectNotExistException();
            }
            Project project = projectRepo.findById(projectId).get();
            sprint.setProject(project);
            repo.save(sprint);

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void updateSprint(Sprint sprint) {
        try {
            writeLock.lock();
            if (!repo.existsById(sprint.getId())) {
                throw new SprintNotExistException();
            }
            if (sprint.getProject() == null || !projectRepo.existsById(sprint.getProject().getId())) {
                throw new ProjectNotExistException();
            }
            repo.save(sprint);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void deleteSprint(long id) {
        try {
            writeLock.lock();
            if (repo.existsById(id)) {
                repo.deleteById(id);
            } else {
                throw new SprintNotExistException();
            }

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public long getProjectIdBy(long sprintId) {
        try {
            writeLock.lock();
            Optional<Sprint> optional = repo.findById(sprintId);
            if (optional.isPresent()) {
                return optional.get().getProject().getId();
            } else {
                throw new SprintNotExistException();
            }
        } finally {
            writeLock.unlock();
        }
    }
}
