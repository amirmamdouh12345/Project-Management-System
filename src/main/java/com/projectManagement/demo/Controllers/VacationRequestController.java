package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.Requests.VacationDecisions.DepartmentManagerVacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationDecisions.HRVacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationDecisions.TeamLeadVacationDecisionRequest;
import com.projectManagement.demo.DTOs.Requests.VacationRequestDTO;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Services.DecisionImplementations.VacationRequestDepManagerDecisionLogic;
import com.projectManagement.demo.Services.DecisionImplementations.VacationRequestHRDecisionLogic;
import com.projectManagement.demo.Services.DecisionImplementations.VacationRequestTechLeadDecisionLogic;
import com.projectManagement.demo.Services.VacationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacationRequest")
public class VacationRequestController {

    @Autowired
    private VacationRequestService vacationRequestService;

    @Autowired
    private VacationRequestTechLeadDecisionLogic techLeadLogic;

    @Autowired
    private VacationRequestDepManagerDecisionLogic depManagerLogic;

    @Autowired
    private VacationRequestHRDecisionLogic hrLogic;


    @GetMapping
    public List<VacationRequest> getAllVacationRequests() {
        return vacationRequestService.getAllVacationRequests();
    }



    @PostMapping
    public String addVacationRequest(@RequestBody VacationRequestDTO vacationRequest) {
        return vacationRequestService.addVacationRequest(vacationRequest);
    }

    //TODO  after adding authentication to users
    @GetMapping("/teamLead/{teamLeadId}")
    public List<VacationRequest> getAllPendingVacationRequestsByTeamLeadId(@PathVariable("teamLeadId") Long teamLeadId) {
        System.out.println("teamLeadId: "+teamLeadId);
        return vacationRequestService.getAllPendingVacationRequestsByTeamLeadId(teamLeadId);
    }

//    //TODO  after adding authentication to users
//    @GetMapping("/ss/{vacationId}")
//    public List<VacationRequest> ss(@PathVariable("vacationId") Long vacationId) {
//        System.out.println("vacationId: "+vacationId);
//        VacationRequest v = vacationRequestService.findById(vacationId);
//
//        List<VacationRequest> r= v.getDepartmentManager().getManagedVacationRequests();
//        return r;
//    }



    //TODO  after adding authentication to users
    @GetMapping("/depManager/{depManagerId}")
    public List<VacationRequest> getAllPendingVacationRequestsByDepManagerId(@PathVariable("depManagerId") Long depManagerId) {
        System.out.println("teamLeadId: "+depManagerId);
        return vacationRequestService.getAllPendingVacationRequestsByDepartmentManager(depManagerId);
    }

    //TODO  after adding authentication to users
    @GetMapping("/hr/{hrId}")
    public List<VacationRequest> getAllPendingVacationRequestsByHR(@PathVariable("hrId") Long hrId) {
        System.out.println("hrId: "+hrId);


        return vacationRequestService.getAllPendingVacationRequestsByHR(hrId);
    }



    //TODO  after adding authentication to users
    @PostMapping("/teamLead/decision")
    public String putTeamLeadDecision(@RequestBody TeamLeadVacationDecisionRequest teamLeadVacationDecisionRequest  ) {

        return vacationRequestService.putDecision(techLeadLogic,teamLeadVacationDecisionRequest);
    }

    //TODO  after adding authentication to users
    @PostMapping("/depManager/decision")
    public String putDepManagerDecision(@RequestBody DepartmentManagerVacationDecisionRequest departmentManagerVacationDecisionRequest) {

        return vacationRequestService.putDecision(depManagerLogic,departmentManagerVacationDecisionRequest);
    }



    //TODO  after adding authentication to users
    @PostMapping("/hr/decision")
    public String putHRDecision(@RequestBody HRVacationDecisionRequest hrVacationDecisionRequest) {
        return vacationRequestService.putDecision(hrLogic,hrVacationDecisionRequest);
    }





}
