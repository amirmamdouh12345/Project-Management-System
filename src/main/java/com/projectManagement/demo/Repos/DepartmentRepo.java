package com.projectManagement.demo.Repos;
import com.projectManagement.demo.Entities.Structure.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo  extends  JpaRepository<Department,Long> {



    Optional<Department> findByDepName(String depName);

//    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<Department> searchByKeyword(@Param("keyword") String keyword);

    boolean existsByDepName(String depName);

//
}
