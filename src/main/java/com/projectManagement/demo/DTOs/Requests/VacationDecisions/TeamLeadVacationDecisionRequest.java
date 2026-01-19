package com.projectManagement.demo.DTOs.Requests.VacationDecisions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamLeadVacationDecisionRequest extends VacationDecisionRequest {

    private Long teamLeadId;


    @Override
    public String toString() {
        return "TeamLeadVacationDecisionRequest{" +
                "teamLeadId=" + teamLeadId +
                ", vacationRequestId=" + vacationRequestId +
                ", decision='" + decision + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }
}
