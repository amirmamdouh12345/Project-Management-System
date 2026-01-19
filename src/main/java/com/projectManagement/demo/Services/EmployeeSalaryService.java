package com.projectManagement.demo.Services;

import com.projectManagement.demo.Entities.EmployeeMonthlyPaidSalary;
import com.projectManagement.demo.Repos.EmployeeSalaryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSalaryService {

    @Autowired
    EmployeeSalaryRepo employeeSalaryRepo;

    public void createSalaryRecord(EmployeeMonthlyPaidSalary employeeSalary) {
        employeeSalaryRepo.save(employeeSalary);
    }

}
