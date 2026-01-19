package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.Structure.Task;
import com.projectManagement.demo.Enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task,Long> {

    @Query(value = "SELECT * FROM task t JOIN ( select employee_Id FROM employee where first_name= :firstName and last_name= :lastName) e \n" +
            "ON t.employee_id = e.employee_id AND task_status = :taskStatus",nativeQuery = true)
    public List<Task> findByEmpName(@Param("firstName") String firstName, @Param("lastName") String lastName,@Param("taskStatus") String taskStatus);

    @Query(value = "SELECT * FROM task t JOIN ( select employee_Id FROM employee where first_name= :firstName and last_name= :lastName) e \n" +
            "ON t.employee_id = e.employee_id ",nativeQuery = true)
    public List<Task> findByEmpName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("""
    SELECT t
    FROM Task t
    WHERE
    (
        (:firstName IS NULL AND :lastName IS NULL)
        OR ( 
            :countSpaces = 1 
            AND t.employee.firstName LIKE :firstName
            AND t.employee.lastName LIKE :lastName
            
        )
        OR (
            :countSpaces = 0
            AND (t.employee.firstName LIKE :firstName
            OR t.employee.lastName LIKE :lastName)
        )
    )
    AND (
        :projectName IS NULL
        OR t.project.projectName LIKE :projectName
    )
    AND (
        :teamName IS NULL
        OR t.team.teamName LIKE :teamName
    )
    AND (
        :taskStatus IS NULL
        OR t.taskStatus = :taskStatus
    )
""")
    List<Task> findAllFilteredTasks(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("projectName") String projectName,
            @Param("teamName") String teamName,
            @Param("taskStatus") TaskStatus taskStatus,
            @Param("countSpaces") int countSpaces
    );




}