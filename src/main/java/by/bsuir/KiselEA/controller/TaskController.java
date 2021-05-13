package by.bsuir.KiselEA.controller;

import by.bsuir.KiselEA.entity.Employee;
import by.bsuir.KiselEA.service.TaskService;
import by.bsuir.KiselEA.entity.Priority;
import by.bsuir.KiselEA.entity.Task;
import by.bsuir.KiselEA.service.EmployeeService;
import by.bsuir.KiselEA.service.PriorityService;
import by.bsuir.KiselEA.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/tasks/")
public class TaskController {

    private final TaskService taskService;
    private final SprintService sprintService;
    private final PriorityService priorityService;
    private final EmployeeService employeeService;

    @Autowired
    public TaskController(TaskService taskService, SprintService sprintService, PriorityService priorityService, EmployeeService employeeService) {
        this.taskService = taskService;
        this.sprintService = sprintService;
        this.priorityService = priorityService;
        this.employeeService = employeeService;
    }

    @GetMapping("signup/{sprint_id}")
    public String showAddTask(@PathVariable("sprint_id") long sprintId, Task task, Model model) {
        model.addAttribute("sprintId", sprintId);
        model.addAttribute("priorities", priorityService.getPriorities());
        model.addAttribute("employees", employeeService.getEmployees());
        return "task/add-task";
    }

    @GetMapping("back/{sprint_id}")
    public String back(@PathVariable("sprint_id") long sprintId) {
        long projectId = sprintService.getProjectIdBy(sprintId);
        return "redirect:/sprints/list/" + projectId;
    }

    @GetMapping("list/{sprint_id}")
    public String showTasks(@PathVariable("sprint_id") long sprintId, Model model) {
        model.addAttribute("tasks", taskService.getTasksBy(sprintId));
        model.addAttribute("sprintId", sprintId);
        return "task/list-task";
    }

    @PostMapping("add/{sprint_id}")
    public String addTask(@PathVariable("sprint_id") long sprintId, @Valid Task task, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sprintId", sprintId);
            model.addAttribute("priorities", priorityService.getPriorities());
            model.addAttribute("employees", employeeService.getEmployees());
            return "task/add-task";
        }
        Priority priority = priorityService.findPriorityById(task.getPriority().getId());
        Employee employee = employeeService.findEmployeeById(task.getEmployee().getId());

        task.setPriority(priority);
        task.setEmployee(employee);

        taskService.saveTask(task, sprintId);

        return "redirect:/tasks/list/" + sprintId;
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        model.addAttribute("priorities", priorityService.getPriorities());
        model.addAttribute("employees", employeeService.getEmployees());
        return "task/update-task";
    }

    @PostMapping("update/{id}")
    public String updateTask(@PathVariable("id") long id, @Valid Task task, BindingResult result, Model model) {
        if (result.hasErrors()) {
            task.setId(id);
            model.addAttribute("task", task);
            model.addAttribute("priorities", priorityService.getPriorities());
            model.addAttribute("employees", employeeService.getEmployees());
            return "task/update-task";
        }

        Priority priority = priorityService.findPriorityById(task.getPriorityTemp());
        Employee employee = employeeService.findEmployeeById(task.getEmployeeTemp());

        task.setPriority(priority);
        task.setEmployee(employee);
        taskService.updateTask(task);

        long sprintId = task.getSprint().getId();
        return "redirect:/tasks/list/" + sprintId;
    }

    @GetMapping("delete/{sprint_id}/{id}")
    public String deleteTask(@PathVariable("sprint_id") long sprintId, @PathVariable("id") long id, Model model) {
        taskService.deleteTask(id);
        return "redirect:/tasks/list/" + sprintId;
    }
}
