package com.projectManagement.demo.Schedulers;

import com.projectManagement.demo.Services.Structure.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BalanceScheduler {

    @Autowired
    private EmployeeService employeeService;



    //day of week ignored , every month , day 1 of month
    @Scheduled(cron = "0 0 0 1 * ?",zone = "Africa/Cairo")
    public void incrementVacationBalance(){
        System.out.println("Incrementing vacation balance");
        employeeService.getAllActivatedEmployees().forEach(employee -> {
            employee.setVacationBalance(employee.getVacationBalance() + 3);
            employee.setSickLeaveBalance(employee.getSickLeaveBalance() + 2);
            employeeService.saveEmployee(employee);
        });
    }

}
