package com.projectManagement.demo.DTOs.Requests.SearchFilters;

import com.projectManagement.demo.Enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskSearchFilterRequest {

    private String teamName;
    private String projectName;
    private String empName;
    private TaskStatus taskStatus;
}
