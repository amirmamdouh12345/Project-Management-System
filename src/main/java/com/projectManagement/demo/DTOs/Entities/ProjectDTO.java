package com.projectManagement.demo.DTOs.Entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectDTO {


    private Long projectId;

    private String projectName;

    private String departmentId;
}
