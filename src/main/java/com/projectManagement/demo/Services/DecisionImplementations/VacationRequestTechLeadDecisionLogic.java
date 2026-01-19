package com.projectManagement.demo.Services.DecisionImplementations;

import com.projectManagement.demo.DTOs.Requests.VacationDecisions.TeamLeadVacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationDecisions.VacationDecisionRequest;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import com.projectManagement.demo.Repos.VacationRequestRepo;
import com.projectManagement.demo.Services.VacationRequestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VacationRequestTechLeadDecisionLogic implements VacationRequestDecisionService{

    @Autowired
    private VacationRequestHistoryService vacationRequestHistoryService;

    @Autowired
    private VacationRequestRepo vacationRequestRepo;


    @Override
    public String putDecision(VacationDecisionRequest request) {

        Long departmentManagerId = ((TeamLeadVacationDecisionRequest) request).getTeamLeadId();

        String decision = request.getDecision();
        Long vacationRequestId = request.getVacationRequestId();
        String rejectionReason = request.getRejectionReason();

        VacationRequest vacationRequest = vacationRequestRepo
                .findOneRequestsForTeamLeadDecision(vacationRequestId
                        ,departmentManagerId
                        , VacationRequestStatus.TEAMLEAD_PENDING)
                .orElseThrow(()->new RuntimeException("No VacationRequests with this ID waits for TeamLead Decision"));

        try{
            if(decision.equals("APPROVE")){
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.TEAMLEAD_APPROVED);
                return "TechLead APPROVED";
            }else if(decision.equals("REJECT")){
                vacationRequest.setRejectionReason(rejectionReason);
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.TEAMLEAD_REJECTED);
                return "TechLead REJECTED";
            }else {
                throw new RuntimeException("Invalid Decision");
            }
        }finally {
            vacationRequestHistoryService.save(vacationRequest , departmentManagerId);

        }
    }
}
