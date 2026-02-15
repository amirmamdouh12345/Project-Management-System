package com.projectManagement.demo.Entities.Structure;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projectManagement.demo.Entities.*;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(unique = true,nullable = false)
    @Email
    private String email;

    @Pattern(
            regexp = "^\\+2\\d{11}$",
            message = "Phone number must be in international format (e.g. +201001234567)"
    )
    private String phoneNumber;

    @OneToMany(mappedBy = "employee" )
    @JsonBackReference("task")
    private List<Task> taskList;

    @OneToMany(mappedBy = "employee" )
    @JsonBackReference("taskComment")
    private List<TaskComment> taskComments;

    @ManyToOne
    @JoinColumn(name = "teamId",referencedColumnName = "teamId")
    @JsonIgnoreProperties(value = {"employees","tasks"})
    private Team team;

    @OneToOne
    @JoinColumn(name = "departmentId", unique = true, nullable = false)
    @JsonIgnoreProperties(value = {"projects","teams"})
    private Department department;

    Timestamp createdAt;

    Boolean active;

    Timestamp deactivationTime;

    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;


    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    // 3 days per month
    Integer vacationBalance;

    //2 days per month
    Integer sickLeaveBalance;

    @OneToMany(mappedBy = "employeeAttendanceId.employee")
    @JsonIgnoreProperties(value = {"employeeAttendanceId.employee"})
    List<EmployeeAttendance> attendanceList;

    Long constantSalary;


    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = {"employee"
            ,"teamLead.vacationRequestList"
            ,"teamLead.managedVacationRequests"
            ,"teamLead.teamLeadVacationRequests"
            ,"departmentManager.vacationRequestList"
            ,"departmentManager.managedVacationRequests"
            ,"departmentManager.teamLeadVacationRequests"
            })
    List<VacationRequest> vacationRequestList;


    // for department managers only
    @OneToMany(mappedBy = "departmentManager")
    @JsonIgnoreProperties(value = {"employee.vacationRequestList"
            ,"employee.managedVacationRequests"
            ,"employee.teamLeadVacationRequests"
            ,"teamLead.vacationRequestList"
            ,"teamLead.managedVacationRequests"
            ,"teamLead.teamLeadVacationRequests"
            ,"departmentManager.vacationRequestList"
            ,"departmentManager.managedVacationRequests"
            ,"departmentManager.teamLeadVacationRequests"
    })
    List<VacationRequest> managedVacationRequests;


    // for teamLeads only
    @OneToMany(mappedBy = "teamLead")
    @JsonIgnoreProperties(value = {"employee.vacationRequestList"
            ,"employee.managedVacationRequests"
            ,"employee.teamLeadVacationRequests"
            ,"teamLead.vacationRequestList"
            ,"teamLead.managedVacationRequests"
            ,"teamLead.teamLeadVacationRequests"
            ,"departmentManager.vacationRequestList"
            ,"departmentManager.managedVacationRequests"
            ,"departmentManager.teamLeadVacationRequests"
    })
    List<VacationRequest> teamLeadVacationRequests;


    @OneToOne(mappedBy = "employee")
    @JsonIgnoreProperties(value = {"employee"})
    private EmployeeMonthlyPaidSalary salary;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +

                ", team=" + team.getTeamName() +
                ", department=" + department.getDepName() +
                ", createdAt=" + createdAt +
                ", active=" + active +
                ", deactivationTime=" + deactivationTime +
                ", jobTitle=" + jobTitle +
                ", employeeRole=" + employeeRole +
                ", vacationBalance=" + vacationBalance +
                ", sickLeaveBalance=" + sickLeaveBalance +
                ", attendanceList=" + attendanceList +
                ", constantSalary=" + constantSalary +
                ", vacationRequestList=" + vacationRequestList +
                ", salary=" + salary +
                '}';
    }


    //    @Override
//    public String toString() {
//        return "Employee{" +
//                "employeeId=" + employeeId +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", taskList=" + taskList +
//                ", taskComments=" + taskComments +
//                ", team=" + team +
//                ", createdAt=" + createdAt +
//                ", active=" + active +
//                ", deactivationTime=" + deactivationTime +
//                ", jobTitle=" + jobTitle +
//                ", employeeRole=" + employeeRole +
//                '}';
//    }
}
