package com.projectManagement.demo.DTOs.Requests;

import com.projectManagement.demo.DTOs.Entities.TaskDTO;
import com.projectManagement.demo.Enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.Task;

@Getter
@Setter
public class AssignNewTaskToEmployee {

    String fullName;

    TaskDTO task;

    String employeeId;

}
