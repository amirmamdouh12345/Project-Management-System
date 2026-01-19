package com.projectManagement.demo.Services.Structure;

import com.projectManagement.demo.Entities.Structure.Project;
import com.projectManagement.demo.Entities.Structure.Task;
import com.projectManagement.demo.Repos.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public Project getProjectById(Long id) {
        Optional<Project> project = projectRepo.findById(id);
        return project.orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public String addProject(Project project) {
        Project saved = projectRepo.save(project);
        if (saved == null) {
            throw new RuntimeException("Project not added");
        }

        List<Task> c= project.getTasks();
        if (c != null){
            for(Task z : c){
                z.setProject(project);

            }

        }
        return "Project added successfully";
    }

    public String updateProject(Long projectId, Project project) {
        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (project == null || !projectOptional.isPresent()) {
            throw new RuntimeException("Project not found");
        }
        List<Task> c= project.getTasks();
        if (c != null){
            for(Task z : c){
                z.setProject(project);
            }

        }


        projectRepo.save(project);
        return "Project updated successfully";
    }

    public String deleteProject(Long projectId) {
        projectRepo.deleteById(projectId);
        return "Project deleted successfully";
    }

    public Project getByProjectName(String projectName){
        Project project = projectRepo.findByProjectName(projectName).orElseThrow(()->new RuntimeException("Project is not found."));
        return project;
    }

}