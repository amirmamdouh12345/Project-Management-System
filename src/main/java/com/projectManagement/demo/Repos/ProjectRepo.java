package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.Structure.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project,Long> {

    Optional<Project> findByProjectName(String projectName);
}