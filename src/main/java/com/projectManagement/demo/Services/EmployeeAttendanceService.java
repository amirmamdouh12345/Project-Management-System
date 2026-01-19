package com.projectManagement.demo.Services;

import com.projectManagement.demo.DTOs.ProjectionsFromDB.EmployeeWorkingHoursProjection;
import com.projectManagement.demo.Entities.CompositeKeys.EmployeeAttendanceId;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.EmployeeAttendance;
import com.projectManagement.demo.Enums.AttendanceStatus;
import com.projectManagement.demo.Repos.EmployeeAttendanceRepo;
//import com.projectManagement.demo.Repos.EmployeeWorkingHoursProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeAttendanceService {

    @Autowired
    private EmployeeAttendanceRepo employeeAttendanceRepo;

    public void createDefaultAttendanceRecord(Employee employee, LocalDate date,AttendanceStatus attendanceStatus){

        // if this day is already OFF for this employee --> don't create a new att record
        if(chekIfEmployeeIsAlreadyOFF(employee,date)){
            return;
        }

        EmployeeAttendance employeeAttendance = new EmployeeAttendance();


        EmployeeAttendanceId employeeAttendanceId = new EmployeeAttendanceId();
        employeeAttendanceId.setEmployee(employee);
        employeeAttendanceId.setAttendanceDate(date);

        employeeAttendance.setEmployeeAttendanceId(employeeAttendanceId);
        employeeAttendance.setHours(0);
        employeeAttendance.setCheckIn(false);
        employeeAttendance.setCheckOut(false);
        employeeAttendance.setAttendanceStatus(attendanceStatus);
        employeeAttendanceRepo.save(employeeAttendance);
    }


    public boolean chekIfEmployeeIsAlreadyOFF(Employee employee, LocalDate date) {

       Optional<EmployeeAttendance> employeeAttendanceOptional = employeeAttendanceRepo.findOFFEmployeeAttendanceByEmployeeIdAndAttendanceDate(employee.getEmployeeId(),date);

       return employeeAttendanceOptional.isPresent();

    }





    public String checkIn(Employee employee){
        Optional<EmployeeAttendance> employeeAttendance =employeeAttendanceRepo.findEmployeeAttendanceByEmployeeIdAndAttendanceDate(
                employee.getEmployeeId(),
                new Date(System.currentTimeMillis()).toLocalDate()
                );
        if(employeeAttendance.isPresent()){
            employeeAttendance.get().setCheckIn(true);
            employeeAttendance.get().setCheckInTime(new Time(System.currentTimeMillis()));
            employeeAttendanceRepo.save(employeeAttendance.get());
            return "Employee " + employee.getEmployeeId() + " checked in successfully";
        }else{
            return "Attendance record not found for employee " + employee.getEmployeeId();
        }
    }

    public  String checkOut(Employee employee){
        Optional<EmployeeAttendance> employeeAttendanceOptional =employeeAttendanceRepo.findEmployeeAttendanceByEmployeeIdAndAttendanceDate(
            employee.getEmployeeId(),new Date(System.currentTimeMillis()).toLocalDate());

        if(employeeAttendanceOptional.isPresent()){
            EmployeeAttendance employeeAttendance = employeeAttendanceOptional.get();
            if(employeeAttendance.isCheckIn()){
                employeeAttendance.setCheckOut(true);
                employeeAttendance.setCheckOutTime(new Time(System.currentTimeMillis()));
                employeeAttendanceRepo.save(employeeAttendance);
                return "Employee " + employee.getEmployeeId() + " checked out successfully";
            }else{
                return "Employee " + employee.getEmployeeId() + " is not checked in";
            }
        }else{
            return "Attendance record not found for employee " + employee.getEmployeeId();

        }
    }

    public void setAttendanceStatus(Employee employee){
        Optional<EmployeeAttendance> employeeAttendanceOptional =employeeAttendanceRepo.findEmployeeAttendanceByEmployeeIdAndAttendanceDate(
            employee.getEmployeeId(),new Date(System.currentTimeMillis()).toLocalDate());

        if(employeeAttendanceOptional.isEmpty()){
            throw new  RuntimeException("Employee " + employee.getEmployeeId() + " has no attendance record");
        }

        if(employeeAttendanceOptional.get().isCheckIn() && employeeAttendanceOptional.get().isCheckOut()){
            EmployeeAttendance employeeAttendance = employeeAttendanceOptional.get();
            long differenceInMilliSeconds = employeeAttendance.getCheckOutTime().getTime() - employeeAttendance.getCheckInTime().getTime();
            long hours = differenceInMilliSeconds / (60 * 60 * 1000);
            employeeAttendance.setAttendanceStatus(hours>8? AttendanceStatus.OVERTIME:
                    hours==8?AttendanceStatus.ATTENDED:
                    AttendanceStatus.DEDUCTED);

            employeeAttendance.setHours(Math.min((int)hours,9));
            employeeAttendanceRepo.save(employeeAttendance);

        }else{
            employeeAttendanceOptional.get().setAttendanceStatus(AttendanceStatus.ABSENT);
            employeeAttendanceOptional.get().setHours(0);

            employeeAttendanceRepo.save(employeeAttendanceOptional.get());
        }

    }


    public List<EmployeeWorkingHoursProjection> getTotalHoursMonthly(LocalDate date){
        int month = date.getMonth().getValue();
        int year =date.getYear();

        System.out.println("month: " + month + " year: " + year);
        List<EmployeeWorkingHoursProjection>  a = employeeAttendanceRepo.findTotalSumoFHoursByEmployeeIdAndAttendanceDate(month,year);
        return a;
    }


}
