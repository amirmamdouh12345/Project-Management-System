package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.Entities.Structure.Project;
import com.projectManagement.demo.Services.Structure.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    public String addProject(@RequestBody Project project) {
        return projectService.addProject(project);
    }

    @PutMapping("/{id}")
    public String updateProject(
            @PathVariable Long id,
            @RequestBody Project project
    ) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }
}