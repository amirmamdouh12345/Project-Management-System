package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.ProjectionsFromDB.EmployeeWorkingHoursProjection;
import com.projectManagement.demo.Services.EmployeeAttendanceService;
import com.projectManagement.demo.Services.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employeeSalary")
public class EmployeeSalaryController {

    @Autowired
    EmployeeSalaryService employeeSalaryService;

    @Autowired
    EmployeeAttendanceService employeeAttendanceService;

    @Autowired
    public EmployeeSalaryController(EmployeeSalaryService employeeSalaryService) {
        this.employeeSalaryService = employeeSalaryService;
    }

    @GetMapping
    List<EmployeeWorkingHoursProjection> getAllEmployeeSalaries(){
        LocalDate today = LocalDate.now();

        return employeeAttendanceService.getTotalHoursMonthly(today);
    }


}
