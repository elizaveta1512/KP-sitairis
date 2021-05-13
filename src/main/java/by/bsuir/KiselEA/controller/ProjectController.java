package by.bsuir.KiselEA.controller;

import by.bsuir.KiselEA.entity.Project;
import by.bsuir.KiselEA.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/projects/")
public class ProjectController {

    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("signup")
    public String showAddProject(Project project) {
        return "project/add-project";
    }

    @GetMapping("list")
    public String showProjects(Model model) {
        model.addAttribute("projects", service.getProjects());
        return "project/list-project";
    }

    @PostMapping("add")
    public String addProject(@Valid Project project, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "project/add-project";
        }
        project.setCreatedDate(new Date());
        service.saveProject(project);

        return "redirect:/projects/list";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Project project = service.findById(id);
        model.addAttribute("project", project);
        return "project/update-project";
    }

    @PostMapping("update/{id}")
    public String updateProject(@PathVariable("id") long id, @Valid Project project, BindingResult result, Model model) {
        if (result.hasErrors()) {
            project.setId(id);
            return "project/update-project";
        }

        service.updateProject(project);

        return "redirect:/projects/list";
    }

    @GetMapping("delete/{id}")
    public String deleteProject(@PathVariable("id") Long id, Model model) {
        service.deleteProject(id);

        return "redirect:/projects/list";
    }
}
