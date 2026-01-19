package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.VacationRequests.VacationRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRequestHistoryRepo extends JpaRepository<VacationRequestHistory,Integer>
{
    public List<VacationRequestHistory> findByEmployee(Long employee);
}
