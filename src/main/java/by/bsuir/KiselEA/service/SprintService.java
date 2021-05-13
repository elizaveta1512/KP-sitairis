package by.bsuir.KiselEA.service;


import by.bsuir.KiselEA.entity.Sprint;

import java.util.Collection;

public interface SprintService {

    Collection<Sprint> getSprintsSortBy(long id);

    Sprint findById(long id);

    void saveSprint(Sprint sprint, long projectId);

    void updateSprint(Sprint sprint);

    void deleteSprint(long id);

    long getProjectIdBy(long sprintId);
}
