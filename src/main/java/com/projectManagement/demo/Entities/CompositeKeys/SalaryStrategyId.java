package com.projectManagement.demo.Entities.CompositeKeys;

import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SalaryStrategyId {

    @Enumerated(EnumType.STRING)
    JobTitle jobTitle;

    @Enumerated(EnumType.STRING)
    EmployeeRole employeeRole;
}

