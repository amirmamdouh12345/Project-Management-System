package com.projectManagement.demo.Services;

import com.projectManagement.demo.DTOs.Entities.TaskCommentDTO;
import com.projectManagement.demo.Entities.Structure.Task;
import com.projectManagement.demo.Entities.TaskComment;
import com.projectManagement.demo.Repos.TaskCommentRepo;
import com.projectManagement.demo.Services.Structure.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class TaskCommentService {

    @Autowired
    private TaskCommentRepo taskCommentRepo;

    @Autowired
    private TaskService taskService;

    public TaskComment getCommentById(Long id) {
        Optional<TaskComment> comment = taskCommentRepo.findById(id);
        return comment.orElseGet(() -> {
            throw new RuntimeException("Comment not found");
        });
    }

    public String addORUpdateComment(TaskCommentDTO comment) {
        TaskComment taskComment = new TaskComment();

        Task v = taskService.getTaskById(comment.getTaskId());


        if(comment.getId() != null) {

            //if exist -> update
            Optional<TaskComment> optionalTaskComment = taskCommentRepo.findById(comment.getId());
            if(optionalTaskComment.isEmpty()){
                throw  new RuntimeException("This Comment can't be updated cause it's not found");
            }

            // create new comment
            else{
                taskComment= optionalTaskComment.get();
                taskComment.setUpdatedAt(Timestamp.from(Instant.now()));

            }
        }else{
            taskComment.setCreatedAt(Timestamp.from(Instant.now()));

        }
        taskComment.setComment(comment.getComment());
        taskComment.setTask(v);


        TaskComment saved = taskCommentRepo.save(taskComment);
        if (saved == null) {
            throw new RuntimeException("Comment not added");
        }
        return "Comment added successfully";
    }

    public String updateComment(Long commentId, TaskComment comment) {
        Optional<TaskComment> commentOptional = taskCommentRepo.findById(commentId);
        if (comment == null || !commentOptional.isPresent()) {
            throw new RuntimeException("Comment not found");
        }
        taskCommentRepo.save(comment);
        return "Comment updated successfully";
    }

    public String deleteComment(Long commentId) {
        taskCommentRepo.deleteById(commentId);
        return "Comment deleted successfully";
    }
}
