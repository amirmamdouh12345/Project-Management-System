package com.projectManagement.demo.Services;

import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequestHistory;
import com.projectManagement.demo.Repos.VacationRequestHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VacationRequestHistoryService {

    @Autowired
    private VacationRequestHistoryRepo vacationRequestHistoryRepo;

    public List<VacationRequestHistory> getVacationRequestHistoryByEmployeeId(Long employeeId){
        return vacationRequestHistoryRepo.findByEmployee(employeeId);
    }


    public void save(VacationRequestHistory vacationRequestHistory){
        vacationRequestHistoryRepo.save(vacationRequestHistory);
    }

    public void save(VacationRequest vacationRequest , Long decisionOwnerId){
        VacationRequestHistory vacationRequestHistory = new VacationRequestHistory();
        vacationRequestHistory.setVacationRequest(vacationRequest);
        vacationRequestHistory.setVacationRequestStatus(vacationRequest.getVacationRequestStatus());
        vacationRequestHistory.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        vacationRequestHistory.setRejectionReason(vacationRequest.getRejectionReason());
        vacationRequestHistory.setDecisionOwnerId(decisionOwnerId);
        vacationRequestHistoryRepo.save(vacationRequestHistory);
    }

}
