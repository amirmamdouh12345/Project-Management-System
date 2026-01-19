package com.projectManagement.demo.DTOs.Requests.VacationDecisions;

import com.projectManagement.demo.DTOs.Requests.VacationRequestDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HRVacationDecisionRequest extends VacationDecisionRequest {

    private Long hrId;

    @Override
    public String toString() {
        return "HRVacationDecisionRequest{" +
                "hrId=" + hrId +
                ", vacationRequestId=" + vacationRequestId +
                ", decision='" + decision + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }
}
