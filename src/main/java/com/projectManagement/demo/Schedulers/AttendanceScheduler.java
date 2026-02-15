package com.projectManagement.demo.Schedulers;

import com.projectManagement.demo.Constants.Constants;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.EmployeeMonthlyPaidSalary;
import com.projectManagement.demo.DTOs.ProjectionsFromDB.EmployeeWorkingHoursProjection;
import com.projectManagement.demo.Enums.AttendanceStatus;
import com.projectManagement.demo.Services.EmployeeAttendanceService;
import com.projectManagement.demo.Services.EmployeeSalaryService;
import com.projectManagement.demo.Services.Structure.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
public class AttendanceScheduler {

    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;

    @Autowired
    private EmployeeService employeeService;


    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    // at start of each day  --> create attendance record for each employee
    @Scheduled(cron = "0 0 0 * * SUN-THU")
//    @Scheduled(cron = "0 15 11 * * *") // for testing
    public void createAttendanceRecordForEachEmployee() {
        System.out.println("createAttendanceRecordForEachEmployee");
        employeeService.getAllActivatedEmployees().forEach(emp -> {
            employeeAttendanceService.createDefaultAttendanceRecord(emp,new Date(System.currentTimeMillis()).toLocalDate(), AttendanceStatus.NOT_ASSIGNED);
        });
    }

    // at the end of each day  --> create set the status of the attendance record for each employee
    @Scheduled(cron = "0 0 23 * * SUN-THU")
//    @Scheduled(cron = "0 43 20 * * *") // for testing
    public void setAttendanceStatusForEachEmployee() {
        System.out.println("setAttendanceStatusForEachEmployee");

        employeeService.getAllActivatedEmployees().forEach(emp -> {
            employeeAttendanceService.setAttendanceStatus(emp);
        });
    }
    // 28/eachMonth/eachYear --> get all working hours and create salary record for each employee
//    @Scheduled(cron = "0 0 23 28 * *")
    @Scheduled(cron = "0 21 10 * * *")
    public void getAllWorkingHoursAndCreateSalaryRecordPerMonth(){
        System.out.println("getAllWorkingHoursAndCreateSalaryRecordPerMonth");
        LocalDate today = LocalDate.now();

        List<EmployeeWorkingHoursProjection> employeeWorkingHours = employeeAttendanceService.getTotalHoursMonthly(today);

        System.out.println(employeeWorkingHours.size());
        employeeWorkingHours.forEach(emp -> {
            createSalaryRecord(emp,today);
        });
    }

    private void createSalaryRecord(EmployeeWorkingHoursProjection workingHoursProjection, LocalDate today) {
        System.out.println("Employee ID: " + workingHoursProjection.getEmployeeId() + " Employee Working Hours: " + workingHoursProjection.getEmployeeWorkingHours() + " All Working Hours: " + workingHoursProjection.getAllWorkingHours());

        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());

        Employee employee = employeeService.getEmployeeById(workingHoursProjection.getEmployeeId());
        EmployeeMonthlyPaidSalary empSalary = new EmployeeMonthlyPaidSalary();
        empSalary.setEmployee(employee);
        empSalary.setMonthYear(yearMonth);
        empSalary.setBasicSalary(employee.getConstantSalary());

        Long workingHours = workingHoursProjection.getEmployeeWorkingHours();
        Long allWorkingHours = workingHoursProjection.getAllWorkingHours();

        Long diffHours = allWorkingHours - workingHours;

        if(diffHours>0) {
            empSalary.setDeductionSalary(diffHours * Constants.DEDUCTION_SALARY);
            empSalary.setOvertimeSalary(0L);
        }else if(diffHours<0){
            empSalary.setOvertimeSalary(Math.abs(diffHours * Constants.DEDUCTION_SALARY));
            empSalary.setDeductionSalary(0L);
        }else{
            empSalary.setDeductionSalary(0L);
            empSalary.setOvertimeSalary(0L);
        }
        Long expectedSalaryThisMonth = empSalary.getBasicSalary() - diffHours * Constants.DEDUCTION_SALARY;

        empSalary.setTotalSalary(expectedSalaryThisMonth);

        employeeSalaryService.createSalaryRecord(empSalary);
    }




}
