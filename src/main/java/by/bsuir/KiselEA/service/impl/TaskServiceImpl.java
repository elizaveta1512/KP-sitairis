package by.bsuir.KiselEA.service.impl;

import by.bsuir.KiselEA.entity.Sprint;
import by.bsuir.KiselEA.entity.Task;
import by.bsuir.KiselEA.exception.SprintNotExistException;
import by.bsuir.KiselEA.exception.TaskNotExistException;
import by.bsuir.KiselEA.repository.SprintRepo;
import by.bsuir.KiselEA.repository.TaskRepository;
import by.bsuir.KiselEA.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final SprintRepo sprintRepository;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, SprintRepo sprintRepository) {
        this.taskRepository = taskRepository;
        this.sprintRepository = sprintRepository;
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        readLock = reentrantReadWriteLock.readLock();
        writeLock = reentrantReadWriteLock.writeLock();
    }

    @Override
    public Collection<Task> getTasksBy(long id) {
        try {
            readLock.lock();
            List<Task> tasks = StreamSupport.stream(taskRepository.findAll().spliterator(), false)
                    .filter(o -> {
                        Sprint s = o.getSprint();
                        return s != null && s.getId() == id;
                    })
                    .sorted(Comparator.comparing(Task::getIsDone).thenComparingInt(o -> o.getPriority().getWeight()))
                    .collect(Collectors.toList());
            if (tasks.isEmpty()) return null;
            for (int i = 0; i <= tasks.size() - 1; i++) {
                tasks.get(i).setNumber(i + 1);
            }
            return tasks;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Task findById(long id) {
        try {
            readLock.lock();
            Optional<Task> optional = taskRepository.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new TaskNotExistException();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void saveTask(Task task, long sprintId) {
        try {
            writeLock.lock();
            if (!sprintRepository.existsById(sprintId)) {
                throw new SprintNotExistException();
            }
            Sprint sprint = sprintRepository.findById(sprintId).get();
            task.setSprint(sprint);
            taskRepository.save(task);

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void updateTask(Task task) {
        try {
            writeLock.lock();
            if (!taskRepository.existsById(task.getId())) {
                throw new TaskNotExistException();
            }
            if (task.getSprint() == null || !sprintRepository.existsById(task.getSprint().getId())) {
                throw new SprintNotExistException();
            }
            taskRepository.save(task);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void deleteTask(long id) {
        try {
            writeLock.lock();
            if (taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
            } else {
                throw new TaskNotExistException();
            }

        } finally {
            writeLock.unlock();
        }
    }
}
