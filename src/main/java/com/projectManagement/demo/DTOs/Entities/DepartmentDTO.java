package com.projectManagement.demo.DTOs.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DepartmentDTO {

    String depName;

    String description;

    Long managerId;

    List<ProjectDTO> projects;
}
