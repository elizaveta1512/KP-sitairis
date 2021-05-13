package by.bsuir.KiselEA.controller;

import by.bsuir.KiselEA.entity.Sprint;
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
@RequestMapping("/sprints/")
public class SprintsController {

    private final SprintService sprintService;

    @Autowired
    public SprintsController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("signup/{project_id}")
    public String showAddSprint(@PathVariable("project_id") long projectId, Sprint sprint, Model model) {
        model.addAttribute("projectId", projectId);
        return "sprint/add-sprints";
    }

    @GetMapping("back")
    public String back() {
        return "redirect:/projects/list/";
    }

    @GetMapping("list/{project_id}")
    public String showSprints(@PathVariable("project_id") long projectId, Model model) {
        model.addAttribute("sprints", sprintService.getSprintsSortBy(projectId));
        model.addAttribute("projectId", projectId);
        return "sprint/list-sprints";
    }

    @PostMapping("add/{project_id}")
    public String addSprint(@PathVariable("project_id") long projectId, @Valid Sprint sprint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sprints", sprintService.getSprintsSortBy(projectId));
            model.addAttribute("projectId", projectId);
            return "sprint/add-sprints";
        }
        sprintService.saveSprint(sprint, projectId);

        model.addAttribute("sprints", sprintService.getSprintsSortBy(projectId));
        model.addAttribute("projectId", projectId);
        return "sprint/list-sprints";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Sprint sprint = sprintService.findById(id);
        model.addAttribute("sprint", sprint);
        return "sprint/update-sprints";
    }

    @PostMapping("update/{id}")
    public String updateSprint(@PathVariable("id") long id, @Valid Sprint sprint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            sprint.setId(id);
            return "sprint/update-sprints";
        }
        sprintService.updateSprint(sprint);

        long projectId = sprint.getProject().getId();
        return "redirect:/sprints/list/" + projectId;
    }

    @GetMapping("delete/{project_id}/{id}")
    public String deleteSprint(@PathVariable("project_id") long projectId, @PathVariable("id") long id, Model model) {
        sprintService.deleteSprint(id);
        return "redirect:/sprints/list/" + projectId;
    }
}
