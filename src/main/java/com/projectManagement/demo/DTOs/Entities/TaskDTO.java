package com.projectManagement.demo.DTOs.Entities;

import com.projectManagement.demo.Enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {

    Long taskId;

    TaskStatus taskStatus;

    String description;
}
