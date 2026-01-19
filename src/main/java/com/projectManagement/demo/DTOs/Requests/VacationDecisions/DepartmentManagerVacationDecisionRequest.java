package com.projectManagement.demo.DTOs.Requests.VacationDecisions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentManagerVacationDecisionRequest extends   VacationDecisionRequest {

    private Long departmentManagerId;


    @Override
    public String toString() {

        return "DepartmentManagerVacationDecisionRequest{" +
                "departmentManagerId=" + departmentManagerId +
                ", vacationRequestId=" + vacationRequestId +
                ", decision='" + decision + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }
}
