package com.projectManagement.demo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectManagement.demo.Entities.CompositeKeys.EmployeeAttendanceId;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
public class EmployeeAttendance {

    @EmbeddedId
    private EmployeeAttendanceId employeeAttendanceId;

    private Integer hours;

    private boolean isCheckIn;

    private boolean isCheckOut;

    Time checkInTime;

    Time checkOutTime;

    @Enumerated(EnumType.STRING)
    AttendanceStatus attendanceStatus;

}
