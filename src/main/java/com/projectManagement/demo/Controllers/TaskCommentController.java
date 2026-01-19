package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.Entities.TaskCommentDTO;
import com.projectManagement.demo.Services.TaskCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taskComment")
public class TaskCommentController {

    @Autowired
    public TaskCommentService taskCommentService;

    //TODO: in Security context, we need to attach the comment to the current logged in employee.
    @PostMapping
    public String addORUpdateComment(@RequestBody TaskCommentDTO comment){
        System.out.println("startAddComment");
        //employee must be the one who I logged in with,     --> in security section
        return taskCommentService.addORUpdateComment(comment);
    }
}
