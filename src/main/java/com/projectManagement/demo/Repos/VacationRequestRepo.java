package com.projectManagement.demo.Repos;

import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRequestRepo extends JpaRepository<VacationRequest, Long> {

    @Query("SELECT v FROM VacationRequest v WHERE v.employee.employeeId = :employeeId AND ( v.vacationRequestStatus != 'APPROVED' OR v.vacationRequestStatus != 'REJECTED')")
    public Optional<VacationRequest> findProcessingRequestsByEmployeeId(Long employeeId);

    @Query("SELECT v FROM VacationRequest v WHERE ( v.vacationRequestStatus LIKE '%_APPROVED' OR v.vacationRequestStatus LIKE '%_REJECTED')")
    public List<VacationRequest> findPollingRequests();



    @Query("SELECT v FROM VacationRequest v WHERE v.teamLead.employeeId = :teamLeadId AND ( v.vacationRequestStatus = :vacationRequestStatus )")
    public List<VacationRequest> findAllPendingRequestsForTeamLeadDecision(@Param("teamLeadId") Long teamLeadId, @Param("vacationRequestStatus") VacationRequestStatus vacationRequestStatus);


    @Query("SELECT v FROM VacationRequest v WHERE v.teamLead.employeeId = :teamLeadId AND vacationRequestId = :vacationRequestId   AND  v.vacationRequestStatus = :vacationRequestStatus ")
    public Optional<VacationRequest> findOneRequestsForTeamLeadDecision( @Param("vacationRequestId") Long vacationRequestId,@Param("teamLeadId") Long teamLeadId , @Param("vacationRequestStatus") VacationRequestStatus vacationRequestStatus);


    @Query("SELECT v FROM VacationRequest v WHERE v.departmentManager.employeeId = :depManagerId AND ( v.vacationRequestStatus = :vacationRequestStatus )")
    public List<VacationRequest> findAllPendingRequestsForManagerDecision(@Param("depManagerId") Long depManagerId, @Param("vacationRequestStatus") VacationRequestStatus vacationRequestStatus);


    @Query("SELECT v FROM VacationRequest v WHERE v.departmentManager.employeeId = :depManagerId AND vacationRequestId = :vacationRequestId   AND  v.vacationRequestStatus = :vacationRequestStatus ")
    public Optional<VacationRequest> findOnePendingRequestsForManagerDecision( @Param("vacationRequestId") Long vacationRequestId,@Param("depManagerId") Long depManagerId , @Param("vacationRequestStatus") VacationRequestStatus vacationRequestStatus);


    @Query("SELECT v FROM VacationRequest v WHERE ( v.vacationRequestStatus = :vacationRequestStatus )")
    public List<VacationRequest> findAllPendingRequestsForHrDecision( @Param("vacationRequestStatus") VacationRequestStatus vacationRequestStatus);


    @Query("SELECT v FROM VacationRequest v WHERE v.vacationRequestId = :vacationRequestId   AND  v.vacationRequestStatus = :vacationRequestStatus ")
    public Optional<VacationRequest>  findOnePendingRequestsForHrDecision( @Param("vacationRequestId") Long vacationRequestId,
                                                                            @Param("vacationRequestStatus") VacationRequestStatus vacationRequestStatus);


    @Query("SELECT v FROM VacationRequest v WHERE v.employee.team.teamId = :teamId")
    public  List<VacationRequest> findAllVacationRequestsRelatedToTeamId(@Param("teamId") Long teamId);


    @Query("SELECT v FROM VacationRequest v WHERE v.employee.department.departmentId = :depId")
    public  List<VacationRequest> findAllVacationRequestsRelatedToDepartmentId(@Param("depId") Long depId);



}
