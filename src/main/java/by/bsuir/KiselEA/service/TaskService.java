package by.bsuir.KiselEA.service;


import by.bsuir.KiselEA.entity.Task;

import java.util.Collection;

public interface TaskService {

    Collection<Task> getTasksBy(long id);

    Task findById(long id);

    void saveTask(Task task, long sprintId);

    void updateTask(Task task);

    void deleteTask(long id);
}
