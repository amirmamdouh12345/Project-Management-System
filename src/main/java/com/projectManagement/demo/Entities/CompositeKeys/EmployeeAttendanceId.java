package com.projectManagement.demo.Entities.CompositeKeys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projectManagement.demo.Convernters.LocalDateConverter;
import com.projectManagement.demo.Entities.Structure.Employee;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Embeddable
public class EmployeeAttendanceId {

    @ManyToOne
    @JoinColumn(name = "employeeId",nullable = false)
    @JsonIgnoreProperties(value = {"taskComments","taskList","vacationRequestList"})
    Employee employee;


//    @Convert(converter = LocalDateConverter.class)
    LocalDate attendanceDate;


}
