package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.Requests.AssignTeam2DepProjByNameRequest;
import com.projectManagement.demo.DTOs.Requests.AssignTeam2DepProjRequest;
import com.projectManagement.demo.Entities.Structure.Team;
import com.projectManagement.demo.Services.Structure.DepartmentService;
import com.projectManagement.demo.Services.Structure.ProjectService;
import com.projectManagement.demo.Services.Structure.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ProjectService projectService;

    @GetMapping
    public List<Team>  getAllTeams(@RequestParam( required = false,name = "depName") String depName , @RequestParam(required = false, name = "projectName")String projectName) {
        System.out.println("ffff" + depName +" " + projectName);
        return teamService.getAllTeams();
//        return teamService.findAllFiltered(depName,projectName);
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable("id") Long id) {
        return teamService.getTeamById(id);
    }

    @PutMapping("/{id}")
    public String updateTeam( @PathVariable Long id, @RequestBody Team team ) {
        return teamService.updateTeam(id, team);
    }

    @DeleteMapping("/{id}")
    public String deleteTeam(@PathVariable Long id) {
        return teamService.deleteTeam(id);
    }

    @PostMapping("byId")
    public String addTeamToDepartment(@RequestBody AssignTeam2DepProjRequest req,DepartmentService departmentService , ProjectService projectService){
        return teamService.addTeamToDepartment(req.getDepId(),req.getProjId(),req.getTeam()
                ,departmentService,projectService);
    }

    @PostMapping("/byName")
    public String addTeamToDepartmentByName(@RequestBody AssignTeam2DepProjByNameRequest req){
        return teamService.addTeamToDepartment(req.getDepName(),req.getProjName(),req.getTeam()
                ,departmentService,projectService);
    }

//    @PostMapping("/lead")
//    public String getTeamLead(@RequestBody AssignTeam2DepProjByNameRequest req){
//        return teamService.addTeamToDepartment(req.getDepName(),req.getProjName(),req.getTeam()
//                ,departmentService,projectService);
//    }

}