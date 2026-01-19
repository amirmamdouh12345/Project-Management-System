package com.projectManagement.demo.Entities.Structure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectManagement.demo.Entities.TaskComment;
import com.projectManagement.demo.Enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId",referencedColumnName = "employeeId")
    @JsonIgnoreProperties(value = {"taskList","taskComments","vacationRequestList","attendanceList","salary"})
    private Employee employee;

    @OneToMany(mappedBy = "task" ,fetch =  FetchType.LAZY)
    @JsonIgnoreProperties(value = {"task","employee"})
    private List<TaskComment> taskCommentList;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "projectId",referencedColumnName = "projectId")

    @JsonIgnoreProperties(value = {"team","tasks"})
    private Project project;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "teamId",referencedColumnName = "teamId")

    @JsonIgnoreProperties(value = {"employees","tasks"})
    private Team team;

    private String description;

    @Enumerated(EnumType.STRING)
    TaskStatus taskStatus;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
