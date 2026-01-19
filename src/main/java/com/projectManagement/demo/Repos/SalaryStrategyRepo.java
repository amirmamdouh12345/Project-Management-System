package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.CompositeKeys.SalaryStrategyId;
import com.projectManagement.demo.Entities.SalaryStrategy;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryStrategyRepo extends JpaRepository<SalaryStrategy, SalaryStrategyId>
{
    List<SalaryStrategy> findBySalaryStrategyIdJobTitle(JobTitle jobTitle);

    List<SalaryStrategy> findBySalaryStrategyIdEmployeeRole(EmployeeRole role);

    Optional<SalaryStrategy> findBySalaryStrategyIdJobTitleAndSalaryStrategyIdEmployeeRole(
            JobTitle jobTitle,
            EmployeeRole role
    );}
