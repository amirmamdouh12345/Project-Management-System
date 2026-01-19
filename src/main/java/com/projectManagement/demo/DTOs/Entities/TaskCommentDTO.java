package com.projectManagement.demo.DTOs.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommentDTO {

    private Long id;

    private Long employeeId;


    private Long taskId;

    private String comment;


}

