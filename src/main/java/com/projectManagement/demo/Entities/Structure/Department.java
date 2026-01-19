package com.projectManagement.demo.Entities.Structure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private String depName;

    private String description;

    @OneToMany(mappedBy = "department",cascade = CascadeType.PERSIST)
    private List<Project> projects;

    @OneToMany(mappedBy = "department",cascade = CascadeType.PERSIST)
    private List<Team> teams;

    //managerRelation
    @OneToOne
    @JoinColumn(name = "departmentManagerId",referencedColumnName = "employeeId", unique = true, nullable = true)
    @JsonIgnoreProperties(value = {"team","taskList","taskComments","vacationRequestList","attendanceList","salary"})
    private Employee depManager;
}
