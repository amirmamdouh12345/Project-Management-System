package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee,Long> {

    @Query("SELECT e FROM Employee e WHERE active = true")
    List<Employee> findAllActivated();

    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName AND active = true")
    Optional<Employee> findActivatedByName(@Param("firstName") String firstName, @Param("lastName") String lastName);


    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName ")
    Optional<Employee> findByName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT e FROM Employee e WHERE e.team.teamName = :teamName AND active = true")
    List<Employee> getEmployeesByTeamName(@Param("teamName") String teamName);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.employeeRole= 'MANAGER' AND e.team.department.departmentId =:depId ")
    Integer countManagersInDepById(@Param("depId") Long depId);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.employeeRole= 'TECHLEAD' AND e.team.teamId =:teamId ")
    Integer countTechLeadInTeamById(@Param("teamId") Long teamId);


    @Query("SELECT e FROM Employee e WHERE e.employeeId =:empId AND ( active=false OR active IS NULL)")
    Optional<Employee> findDeactivatedEmployee(@Param("empId") Long empId);


    @Query("SELECT e FROM Employee e WHERE e.employeeId =:empId AND ( active=true OR active IS NULL )")
    Optional<Employee> findActivatedEmployee(@Param("empId") Long empId);


    @Query("SELECT e FROM Employee e WHERE e.employeeId =:empId AND e.jobTitle = :jobTitle ")
    Optional<Employee> findEmployeeRoleById(@Param("empId") Long empId,@Param("jobTitle") JobTitle jobTitle);






}
