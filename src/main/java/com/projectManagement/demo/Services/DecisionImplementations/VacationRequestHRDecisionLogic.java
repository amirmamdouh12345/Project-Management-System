package com.projectManagement.demo.Services.DecisionImplementations;

import com.projectManagement.demo.DTOs.Requests.VacationDecisions.HRVacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationDecisions.VacationDecisionRequest;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Enums.AttendanceStatus;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import com.projectManagement.demo.Repos.VacationRequestRepo;
import com.projectManagement.demo.Services.EmployeeAttendanceService;
import com.projectManagement.demo.Services.Structure.EmployeeService;
import com.projectManagement.demo.Services.VacationRequestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class VacationRequestHRDecisionLogic implements VacationRequestDecisionService {

    @Autowired
    private VacationRequestHistoryService vacationRequestHistoryService;

    @Autowired
    private VacationRequestRepo vacationRequestRepo;

    @Autowired
    private EmployeeAttendanceService employeeAttendanceService;

    @Autowired
    private EmployeeService employeeService;


    @Override
    public String putDecision(VacationDecisionRequest request) {

        Long hrId = ((HRVacationDecisionRequest) request).getHrId();

        //validate htId is a real HR
        employeeService.checkIfEmployeeIsHr(hrId);


        String decision = request.getDecision();
        Long vacationRequestId = request.getVacationRequestId();
        String rejectionReason = request.getRejectionReason();

        VacationRequest vacationRequest = vacationRequestRepo
                .findOnePendingRequestsForHrDecision(vacationRequestId
                        , VacationRequestStatus.HR_PENDING)
                .orElseThrow(()->new RuntimeException("No VacationRequests with this ID waits for HR Decision"));



        try{
            if(decision.equals("APPROVE")){
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.APPROVED);

                // make these days off for the employee
                boolean isCreatedInDB = makeDaysOffForEmployee(vacationRequest);

                if(!isCreatedInDB){
                    throw  new RuntimeException("VacationRequest with ID " + vacationRequestId + " was not created");
                }

                return "HR APPROVED";
            }else if(decision.equals("REJECT")){
                vacationRequest.setRejectionReason(rejectionReason);
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.APPROVED);
                return "HR REJECTED";
            }else {
                throw new RuntimeException("Invalid Decision");
            }
        }finally {
            vacationRequestHistoryService.save(vacationRequest , hrId);
            Employee employee = vacationRequest.getEmployee();
            minusVacationDaysFromBalance(vacationRequest);
            employeeService.saveEmployee(employee);
        }


    }

    private void minusVacationDaysFromBalance(VacationRequest vacationRequest ){
        Employee employee = vacationRequest.getEmployee();
        LocalDate startDate = vacationRequest.getStartDate();
        LocalDate endDate = vacationRequest.getEndDate();
        int noOfDays = endDate.getDayOfYear() - startDate.getDayOfYear() ;
        employee.setVacationBalance(employee.getVacationBalance() - noOfDays);
    }

    @Transactional
    public boolean makeDaysOffForEmployee(VacationRequest vacationRequest){

        LocalDate startDate = vacationRequest.getStartDate();
        LocalDate endDate = vacationRequest.getEndDate();



        for (LocalDate s = startDate; s.isBefore(endDate); s = s.plusDays(1)) {

            DayOfWeek dayOfWeek = s.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                continue;
            }

            employeeAttendanceService.createDefaultAttendanceRecord(vacationRequest.getEmployee(), s, AttendanceStatus.OFF);


        }

        return true;
    }
}
