package com.projectManagement.demo.Repos;

import com.projectManagement.demo.DTOs.ProjectionsFromDB.EmployeeWorkingHoursProjection;
import com.projectManagement.demo.DTOs.ProjectionsFromDB.EmployeeWorkingHoursProjectionInterface;
import com.projectManagement.demo.Entities.EmployeeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EmployeeAttendanceRepo  extends JpaRepository<EmployeeAttendance,Integer>
{

    @Query("SELECT e FROM EmployeeAttendance e WHERE e.employeeAttendanceId.employee.employeeId = :empId AND e.employeeAttendanceId.attendanceDate = :attendanceDate and e.attendanceStatus != 'OFF'")
    Optional<EmployeeAttendance> findEmployeeAttendanceByEmployeeIdAndAttendanceDate(@Param("empId") Long empId, @Param("attendanceDate") LocalDate attendanceDate);


    @Query("SELECT e FROM EmployeeAttendance e WHERE e.employeeAttendanceId.employee.employeeId = :empId AND e.employeeAttendanceId.attendanceDate = :attendanceDate AND e.attendanceStatus = 'OFF'")
    Optional<EmployeeAttendance> findOFFEmployeeAttendanceByEmployeeIdAndAttendanceDate(@Param("empId") Long empId, @Param("attendanceDate") LocalDate attendanceDate);

    @Query("""
        SELECT new com.projectManagement.demo.DTOs.ProjectionsFromDB.EmployeeWorkingHoursProjection(
            a.employeeAttendanceId.employee.employeeId,
            SUM(a.hours),
            8 * COUNT(CASE WHEN a.attendanceStatus != 'OFF' THEN 1 END)
        )
        FROM EmployeeAttendance a
        WHERE a.employeeAttendanceId.attendanceDate.getMonth() = :month AND a.employeeAttendanceId.attendanceDate.getYear() = :year
        GROUP BY a.employeeAttendanceId.employee.employeeId
        """)
    public List<EmployeeWorkingHoursProjection> findTotalSumoFHoursByEmployeeIdAndAttendanceDate(@Param("month") int month, @Param("year") int year);
}
