package com.projectManagement.demo.Schedulers;

import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequestHistory;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import com.projectManagement.demo.Services.VacationRequestHistoryService;
import com.projectManagement.demo.Services.VacationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class VacationRequestsSchedulers {

    private final VacationRequestService vacationRequestService;

    private final VacationRequestHistoryService vacationRequestHistoryService;


    @Autowired
    public VacationRequestsSchedulers(VacationRequestService vacationRequestService, VacationRequestHistoryService vacationRequestHistoryService) {
        this.vacationRequestService = vacationRequestService;
        this.vacationRequestHistoryService = vacationRequestHistoryService;
    }

    @Scheduled(cron = "1 * * * * *")
    public void updateVacationRequests() {
        System.out.println("updateVacationRequests");
        List<VacationRequest> vacationRequests = vacationRequestService.getPollingRequests();

        for (VacationRequest vacationRequest : vacationRequests) {
            if (vacationRequest.getVacationRequestStatus().equals(VacationRequestStatus.TEAMLEAD_APPROVED)) {
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.DEPARTMENT_MANAGER_PENDING);
            } else if(vacationRequest.getVacationRequestStatus().equals(VacationRequestStatus.DEPARTMENT_MANAGER_APPROVED)) {
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.HR_PENDING);
            }else if (vacationRequest.getVacationRequestStatus().equals(VacationRequestStatus.DEPARTMENT_MANAGER_REJECTED)) {
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.REJECTED);
            }
            else if (vacationRequest.getVacationRequestStatus().equals(VacationRequestStatus.TEAMLEAD_REJECTED)) {
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.REJECTED);
            }
            boolean updated= vacationRequestService.updateVacationRequest(vacationRequest);

            if(updated){
                System.out.println("Vacation Request "+vacationRequest+ " Updated");

            }


        }


    }
}
