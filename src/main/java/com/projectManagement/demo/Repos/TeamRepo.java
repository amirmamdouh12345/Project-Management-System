package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.Structure.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepo extends JpaRepository<Team,Long> {

    public Optional<Team> findByTeamName(String teamName);

    @Query("SELECT t FROM Team t WHERE t.teamName = :teamName AND t.department.departmentId = :depId")
    public Optional<Team> findByTeamNameAndDepartmentId(String teamName,Long depId);

    @Query("SELECT t FROM Team t WHERE t.teamId = :teamId AND t.department.departmentId = :depId")
    public Optional<Team> findByTeamIdAndDepartmentId(Long teamId,Long depId);


//    @Query("""
//    SELECT t FROM Team t
//    WHERE (:depName IS NULL OR t.department.depName = :depName)
//      AND (:projName IS NULL OR t.project.projectName = :projName)
//""")
//    List<Team> findAllFiltered(
//            @Param("depName") String depName,
//            @Param("projName") String projectName
//    );

    @Query(value = """
    SELECT * FROM team t 
    WHERE (:depName IS NULL OR t.department_id IN (
     SELECT d.department_id
        FROM department d
        WHERE d.dep_name LIKE :depName
    ))
    AND (:projName IS NULL OR t.project_id IN (
     SELECT p.project_id 
        FROM project p
        WHERE p.project_name LIKE :projName
    ))
""",nativeQuery = true)
    public List<Team> findAllFiltered(@Param("depName") String depName ,@Param("projName") String projectName);




}
