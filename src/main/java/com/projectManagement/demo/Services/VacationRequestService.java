package com.projectManagement.demo.Services;

import com.projectManagement.demo.DTOs.Requests.VacationDecisions.TeamLeadVacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationDecisions.VacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationRequestDTO;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Team;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import com.projectManagement.demo.Enums.VacationRequestStatus;
import com.projectManagement.demo.Enums.VacationType;
import com.projectManagement.demo.Repos.VacationRequestRepo;
import com.projectManagement.demo.Services.DecisionImplementations.VacationRequestDecisionService;
import com.projectManagement.demo.Services.Structure.EmployeeService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class VacationRequestService {

    private final VacationRequestRepo vacationRequestRepo;

    private final EmployeeService employeeService;

    private final VacationRequestHistoryService vacationRequestHistoryService;

    public VacationRequestService(VacationRequestRepo vacationRequestRepo, EmployeeService employeeService, VacationRequestHistoryService vacationRequestHistoryService) {
        this.vacationRequestRepo = vacationRequestRepo;
        this.employeeService = employeeService;
        this.vacationRequestHistoryService = vacationRequestHistoryService;
    }

    public boolean updateVacationRequest(VacationRequest vacationRequest){
        vacationRequestRepo.save(vacationRequest);
        return true;
    }


    public List<VacationRequest> getAllVacationRequests() {
        return vacationRequestRepo.findAll();
    }

    public VacationRequest findById(Long vacationRequestId) {
        return vacationRequestRepo.findById(vacationRequestId).orElseThrow();
    }


    public VacationRequest getProcessingVacationRequestByEmployeeId(Long empId) {
        Optional<VacationRequest> vacationRequestOptional = vacationRequestRepo.findProcessingRequestsByEmployeeId(empId);
        return vacationRequestOptional.orElseGet(()->null);
    }

    public String addVacationRequest(VacationRequestDTO vacationRequestDTO) {

        System.out.println(vacationRequestDTO);



        // check if there's an already vacation request for the employee
        VacationRequest vacationRequestOptional = getProcessingVacationRequestByEmployeeId(vacationRequestDTO.getEmployeeId());

        //check if there's a vacation request already for the employee
        if(vacationRequestOptional!=null){
            return "There's a Vacation Request already for this Employee still in "+ vacationRequestOptional.getVacationRequestStatus().getApplicationStatus()+" status.";
        }

        if(!validateStartAndEndDate(vacationRequestDTO)){
            return "Invalid Start or End Date";
        }

        // validate vacation days with the employee's vacation balance
        Employee employee = employeeService.getEmployeeById(vacationRequestDTO.getEmployeeId());

        VacationRequest vacationRequest = new VacationRequest();


        LocalDate startDate =vacationRequestDTO.getStartDate();
        LocalDate endDate =vacationRequestDTO.getEndDate();

        System.out.println(employee);
        System.out.println(employee.getVacationBalance());
        System.out.println(getNoOfDays(vacationRequestDTO));


        long noOfDays =0;
        System.out.println("what is VacationType()");

        if (vacationRequestDTO.getVacationType().equals(VacationType.ANNUAL)) {
           noOfDays = getNoOfDays(vacationRequestDTO);
            if (noOfDays > employee.getVacationBalance()) {
                return "Vacation Days Exceeded";
            }
        }else if (vacationRequestDTO.getVacationType().equals(VacationType.SICK)) {
            noOfDays = getNoOfDays(vacationRequestDTO);
            if (noOfDays > employee.getSickLeaveBalance()) {
                return "Sick Leave Balance Exceeded";
            }
        }
        //other vacation types have there own no of days
        else {
            noOfDays =vacationRequestDTO.getVacationType().getNoOfDays();
            endDate = ChronoUnit.DAYS.addTo(startDate,noOfDays);

            //calculate end date

        }

        System.out.println("vacationRequestDTO: "+startDate+" "+endDate);


        vacationRequest.setEmployee(employee);
        vacationRequest.setStartDate(startDate);
        vacationRequest.setEndDate(endDate);
        vacationRequest.setVacationType(vacationRequestDTO.getVacationType());
        vacationRequest.setTeamLead(getTeamLead(employee.getTeam()));
        vacationRequest.setDepartmentManager(getDepartmentManager(employee.getTeam())) ;
        vacationRequest.setVacationReason(vacationRequestDTO.getReason());
        vacationRequest.setCreatedAt(Date.valueOf(LocalDate.now()));

        // set the vacation request status
        //Employee --> vacationRequest starts from Techlead
        //TechLead --> vacationRequest starts from Manager
        //Manager  --> VacationRequest starts from HR directly
        //HR --> HR Manager - in another say Manager of HR Department
        if(employee.getEmployeeRole()== EmployeeRole.TECHLEAD || employee.getJobTitle()== JobTitle.HR)
            vacationRequest.setVacationRequestStatus(VacationRequestStatus.DEPARTMENT_MANAGER_PENDING);
        else if (employee.getEmployeeRole()== EmployeeRole.MANAGER) {
            vacationRequest.setVacationRequestStatus(VacationRequestStatus.HR_PENDING);

            //doesn't have team --> so obviously doesn't have team lead
            vacationRequest.setTeamLead(null);

        }else {
            vacationRequest.setVacationRequestStatus(VacationRequestStatus.TEAMLEAD_PENDING);
        }


        System.out.println("vacationRequest: "+ vacationRequest.getStartDate()+" "+vacationRequest.getEndDate() );


        vacationRequestRepo.save(vacationRequest);
        return "Vacation Request Added Successfully";
    }


    public List<VacationRequest> getPollingRequests(){
        return vacationRequestRepo.findPollingRequests();
    }

    public List<VacationRequest> getAllPendingVacationRequestsByDepartmentManager(Long depManager){
        return vacationRequestRepo.findAllPendingRequestsForManagerDecision(depManager,VacationRequestStatus.DEPARTMENT_MANAGER_PENDING);
    }

    public List<VacationRequest> getAllPendingVacationRequestsByHR(Long hrId){
        //validate hr Id
        System.out.println("hrId: "+hrId);
        try {
            Employee v = employeeService.checkIfEmployeeIsHr(hrId);
        }catch (RuntimeException runtimeException){
            throw new RuntimeException("Invalid HR ID");
        }

        // hr is validated well
        return vacationRequestRepo.findAllPendingRequestsForHrDecision(VacationRequestStatus.HR_PENDING);
    }


    public List<VacationRequest> getAllPendingVacationRequestsByTeamLeadId(Long teamLeadId){
        return vacationRequestRepo.findAllPendingRequestsForTeamLeadDecision(teamLeadId,VacationRequestStatus.TEAMLEAD_PENDING);
    }



    public String teamLeadDecision(TeamLeadVacationDecisionRequest teamLeadDecision){
        String decision = teamLeadDecision.getDecision();
        Long vacationRequestId = teamLeadDecision.getVacationRequestId();
        Long teamLeadId = teamLeadDecision.getTeamLeadId();
        String rejectionReason = teamLeadDecision.getRejectionReason();

        System.out.println(teamLeadDecision);
        VacationRequest vacationRequest = getOneVacationRequestsByTeamLeadId(vacationRequestId,teamLeadId);

        try{
            if(decision.equals("APPROVE")){
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.TEAMLEAD_APPROVED);
                return "Team Lead APPROVED";
            }else if(decision.equals("REJECT")){
                vacationRequest.setRejectionReason(rejectionReason);
                vacationRequest.setVacationRequestStatus(VacationRequestStatus.TEAMLEAD_REJECTED);
                return "Team Lead REJECTED";
            }else {
                throw new RuntimeException("Invalid Decision");
            }
        }finally {
            vacationRequestHistoryService.save(vacationRequest , teamLeadId);

        }
    }

    private VacationRequest getOneVacationRequestsByTeamLeadId(Long vacationRequestId ,Long teamLeadId){
        Optional<VacationRequest> vacationRequestOptional = vacationRequestRepo.findOneRequestsForTeamLeadDecision(vacationRequestId,teamLeadId,VacationRequestStatus.TEAMLEAD_PENDING);
        return vacationRequestOptional.orElseThrow(()->new RuntimeException("No Vacation Requests with this ID waits for Team Lead Decision"));
    }

                                        // it contains logic                                                request body
    public String putDecision(VacationRequestDecisionService vacationRequestDecisionService, VacationDecisionRequest decisionRequest){
        return vacationRequestDecisionService.putDecision(decisionRequest);
    }




    private VacationRequest getOneVacationRequestsByDepartmentManager(Long vacationRequestId ,Long departmentManagerId){
        Optional<VacationRequest> vacationRequestOptional = vacationRequestRepo.findOnePendingRequestsForManagerDecision(vacationRequestId,departmentManagerId,VacationRequestStatus.DEPARTMENT_MANAGER_PENDING);
        return vacationRequestOptional.orElseThrow(()->new RuntimeException("No Vacation Requests with this ID waits for Team Lead Decision"));
    }







    private boolean validateStartAndEndDate(VacationRequestDTO vacationRequestDTO){
        LocalDate start = vacationRequestDTO.getStartDate();
        LocalDate end = vacationRequestDTO.getEndDate();
        LocalDate now = Date.valueOf(LocalDate.now()).toLocalDate();
        if(start.isAfter(end)){
            System.out.println("start.after(end)");
            return false;
            // start or end are in past
        }else if (now.isAfter(start) || now.isAfter(end)){
            System.out.println("now.after(start) || now.after(end)");

            return false;
        }
        return true;
    }

    private long getNoOfDays(VacationRequestDTO vacationRequestDTO){
        System.out.println("getNoofDays");
        return ChronoUnit.DAYS.between(vacationRequestDTO.getStartDate(), vacationRequestDTO.getEndDate());
    }

    private Employee getTeamLead(Team team){
        return team.getTechLead();
    }

    private Employee getDepartmentManager(Team team){
        return team.getDepartment().getDepManager();
    }

}
