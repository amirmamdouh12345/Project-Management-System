package com.projectManagement.demo.Entities.VacationRequests;

import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class VacationRequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationRequestHistoryId;

    @ManyToOne
    @JoinColumn(name = "employee_id")

    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vacation_request_id")
    private VacationRequest vacationRequest;

    @Column(name = "vacation_request_status")
    @Enumerated(EnumType.STRING)
    private VacationRequestStatus vacationRequestStatus;

    private String rejectionReason;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private Long decisionOwnerId;

}
