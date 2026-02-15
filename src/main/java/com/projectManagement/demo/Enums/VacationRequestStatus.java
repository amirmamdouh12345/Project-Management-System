package com.projectManagement.demo.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum VacationRequestStatus {

    TEAMLEAD_PENDING("Pending","Pending for Team Lead Approval"),
    TEAMLEAD_APPROVED("Team Lead Approved","Approved by Team Lead and Pending for Department Manager Approval"),
    TEAMLEAD_REJECTED("Team Lead Rejected","Rejected by Team Lead"),


    DEPARTMENT_MANAGER_PENDING("Department Manager Pending","Pending for Department Manager Approval"),
    DEPARTMENT_MANAGER_APPROVED("Department Manager Approved","Approved by Department Manager and Pending for HR Approval"),
    DEPARTMENT_MANAGER_REJECTED("Department Manager Rejected","Rejected by Department Manager"),

    HR_PENDING("HR Pending","Pending for HR Approval"),
    APPROVED("HR Approved","Approved by HR"),
    REJECTED("HR Rejected","Rejected by HR");

    String applicationStatus;
    String description;


}
