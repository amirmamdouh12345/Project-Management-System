package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.EmployeeMonthlyPaidSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSalaryRepo extends JpaRepository<EmployeeMonthlyPaidSalary, Long>
{
}
