package com.projectManagement.demo.Entities.VacationRequests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projectManagement.demo.Convernters.LocalDateConverter;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import com.projectManagement.demo.Enums.VacationType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationRequestId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties(value = {
            "vacationRequestList",
            "attendanceList",
            "taskList",
            "taskComments",
            "team",
            "department"
            ,"managedVacationRequests"
            ,"teamLeadVacationRequests"})
    Employee employee;

//    @Convert(converter = LocalDateConverter.class)
    LocalDate startDate;

//    @Convert(converter = LocalDateConverter.class)
    LocalDate endDate;

    @Nullable
    String vacationReason;

    @Nullable
    String rejectionReason;


    @ManyToOne
    @JoinColumn(name = "team_lead_id",nullable = false)
    @JsonIgnoreProperties(value = {
            "vacationRequestList",
            "attendanceList",
            "taskList",
            "taskComments",
            "team",
            "department"
            ,"managedVacationRequests"
            ,"teamLeadVacationRequests"})
    Employee teamLead;

    @ManyToOne
    @JoinColumn(name = "dep_manager_id",nullable = false)
//    @JsonManagedReference("vacation")
    @JsonIgnoreProperties(value = {
                                    "vacationRequestList",
                                    "attendanceList",
                                    "taskList",
                                    "taskComments",
                                    "team",
                                    "department"
                                    ,"managedVacationRequests"
                                    ,"teamLeadVacationRequests"})
    Employee departmentManager;

    @Enumerated(EnumType.STRING)
    VacationRequestStatus vacationRequestStatus;

    @Enumerated(EnumType.STRING)
    VacationType vacationType;

//    @Convert(converter = LocalDateConverter.class)
    Date createdAt;

    @Override
    public String toString() {
        return "VacationRequest{" +
                "vacationRequestId=" + vacationRequestId +
                ", employee=" + employee +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", vacationReason='" + vacationReason + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", teamLead=" + teamLead +
                ", departmentManager=" + departmentManager +
                ", vacationRequestStatus=" + vacationRequestStatus +
                ", vacationType=" + vacationType +
                ", createdAt=" + createdAt +
                '}';
    }
}
