package com.projectManagement.demo.Entities.Structure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String projectName;

    @ManyToOne
    @JoinColumn(name = "departmentId",referencedColumnName = "departmentId")
    @JsonIgnoreProperties(value = {"projects","teams"})
    private Department department;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = {"project"})

    private List<Team> teams;

    @OneToMany(mappedBy = "project",cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = {"project"})
    private List<Task> tasks;


}
