package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCommentRepo extends JpaRepository<TaskComment,Long> {
}
