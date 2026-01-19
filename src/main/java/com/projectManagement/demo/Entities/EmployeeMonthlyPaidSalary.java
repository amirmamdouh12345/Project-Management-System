package com.projectManagement.demo.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectManagement.demo.Convernters.YearMonthConverter;
import com.projectManagement.demo.Entities.Structure.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Entity
@Getter
@Setter
public class EmployeeMonthlyPaidSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @ManyToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "employeeId")
    @JsonManagedReference("salary-employee")
    private Employee employee;

    @Convert(converter = YearMonthConverter.class)
    private YearMonth monthYear;

    private Long basicSalary;

    private Long deductionSalary;

    private Long overtimeSalary;

    private Long totalSalary;
}
