package by.bsuir.KiselEA.service;


import by.bsuir.KiselEA.entity.Priority;

import java.util.Collection;

public interface PriorityService {

    Collection<Priority> getPriorities();

    Priority findPriorityById(Long id);
}
