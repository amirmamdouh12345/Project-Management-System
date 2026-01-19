package com.projectManagement.demo.DTOs.Requests.VacationDecisions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class VacationDecisionRequest {

    protected Long vacationRequestId;
    protected String decision;
    protected String rejectionReason;
}

