package com.projectManagement.demo.DTOs.ProjectionsFromDB;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor   // mandatory or it will cause error in db
@NoArgsConstructor
public class EmployeeWorkingHoursProjection {

    Long employeeId;

    Long employeeWorkingHours;

    Long allWorkingHours;


    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getEmployeeWorkingHours() {
        return employeeWorkingHours;
    }

    public Long getAllWorkingHours() {
        return allWorkingHours;
    }
}
