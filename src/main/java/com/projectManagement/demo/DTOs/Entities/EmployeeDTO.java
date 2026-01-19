package com.projectManagement.demo.DTOs.Entities;

import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeDTO {

    //in case already exists
    Long employeeId;

    String firstName;

    String lastName;

    String email;

    String phoneNumber;

    Long departmentId;

    // assign to team using name
    String teamName;

    // OR assign to team using Id
    Long teamId;

    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", teamName='" + teamName + '\'' +
                ", teamId=" + teamId +
                ", jobTitle=" + jobTitle +
                ", employeeRole=" + employeeRole +
                '}';
    }
}
