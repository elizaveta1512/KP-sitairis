package by.bsuir.KiselEA.service;


import by.bsuir.KiselEA.entity.Project;

import java.util.Collection;

public interface ProjectService {

    Collection<Project> getProjects();

    Project findById(long id);

    void saveProject(Project project);

    void updateProject(Project project);

    void deleteProject(long id);
}
