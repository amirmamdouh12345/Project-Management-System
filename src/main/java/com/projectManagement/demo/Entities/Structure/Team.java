package com.projectManagement.demo.Entities.Structure;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long teamId;

    String teamName;

    @OneToMany(mappedBy = "team")

    @JsonIgnoreProperties(value = {"team","taskList","taskComments"})
    List<Employee> employees;

    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties(value = {"team","taskList","taskComments"})
    List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "departmentId",referencedColumnName = "departmentId")
    @JsonIgnoreProperties(value = {"teams","projects"})
    Department department;

    @ManyToOne
    @JoinColumn(name = "projectId",referencedColumnName = "projectId")
    @JsonIgnoreProperties(value = {"teams","projects","department"})
    Project project;

    @OneToOne
    @JsonIgnoreProperties(value = {"taskList","taskComments","team"})
    Employee techLead;

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName +
                ", employees=" + employees +
                ", tasks=" + tasks +
                ", department=" + department +
                ", project=" + project +
                '}';
    }
}
