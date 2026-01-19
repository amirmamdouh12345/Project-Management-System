package com.projectManagement.demo.Entities;

import com.projectManagement.demo.Entities.CompositeKeys.SalaryStrategyId;
import com.projectManagement.demo.Enums.JobTitle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SalaryStrategy {

    @EmbeddedId
    SalaryStrategyId salaryStrategyId;

    Long basicSalary;

    Long annualGrowthSalary;

}
