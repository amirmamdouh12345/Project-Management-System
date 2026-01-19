package com.projectManagement.demo.DTOs.Requests;

import com.projectManagement.demo.Enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllTasksForEmployeeRequest {

    String fullName;

    TaskStatus taskStatus;

}
