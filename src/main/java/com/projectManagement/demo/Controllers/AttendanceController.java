package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.Entities.EmployeeAttendance;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Services.EmployeeAttendanceService;
import com.projectManagement.demo.Services.Structure.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;

    @Autowired
    private EmployeeService employeeService;



    // TODO: get the current Employee after adding authentication
    @PostMapping("/checkIn/{empId}")
    public String checkIn(@PathVariable("empId") Long empId){
        // the current Employee

        Employee employee = employeeService.getEmployeeById(empId);// Amir Mamdouh

        return employeeAttendanceService.checkIn(employee);

    }

    // TODO: get the current Employee after adding authentication
    @PostMapping("/checkOut/{empId}")
    public String checkout(@PathVariable("empId") Long empId){
        // the current Employee

        Employee employee = employeeService.getEmployeeById(empId);// Amir Mamdouh

        return employeeAttendanceService.checkOut(employee);

    }

    // TODO: get the current Employee after adding authentication
    @GetMapping("/a")
    public List<EmployeeAttendance> getAllAttendance(){
        // the current Employee


        return employeeAttendanceService.get();

    }


}
