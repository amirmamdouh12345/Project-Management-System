package com.projectManagement.demo.Entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Task;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class TaskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employeeId",referencedColumnName = "employeeId")
    @JsonManagedReference("taskComment")
    @Nullable()
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "taskId",referencedColumnName = "taskId")
    @JsonManagedReference("task-taskComment")
    private Task task;

    private String comment;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
