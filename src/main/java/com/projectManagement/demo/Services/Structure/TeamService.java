package com.projectManagement.demo.Services.Structure;

import com.projectManagement.demo.Entities.Structure.Department;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Project;
import com.projectManagement.demo.Entities.Structure.Team;
import com.projectManagement.demo.Repos.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepo teamRepo;


    public List<Team> getAllTeams() {
        return teamRepo.findAll();
    }

    public Team getTeamById(Long id) {
        Optional<Team> team = teamRepo.findById(id);
        return team.orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public Team getTeamByTeamName(String teamName) {
        Optional<Team> team = teamRepo.findByTeamName(teamName);
        return team.orElseThrow(() -> new RuntimeException("Team not found"));
    }

    public Team getTeamByTeamIdAndDepartment(Long teamId,Long depId) {
        System.out.println("getTeamByTeamIdAndDepartment");
        Optional<Team> team = teamRepo.findByTeamIdAndDepartmentId(teamId,depId);
        System.out.println("finished getTeamByTeamIdAndDepartment");

        return team.orElseThrow(() -> new RuntimeException("Team not found in the department"));
    }

    public Team getTeamByTeamNameAndDepartment(String teamName,Long depId) {
        Optional<Team> team = teamRepo.findByTeamNameAndDepartmentId(teamName,depId);
        return team.orElseThrow(() -> new RuntimeException("Team not found in the department"));
    }

//    public String addTeam(Team team) {
//
//        //couldn't be unassigned
//        Department dep = team.getDepartment();
//
//        //project could be unassigned
//        Project project =team.getProject();
//
//        if(dep!=null){
//            dep.setTeams();
//        }
//
//
//        Team saved = teamRepo.save(team);
//        if (saved == null) {
//            throw new RuntimeException("Team not added");
//        }
//        return "Team added successfully";
//    }

    public String updateTeam(Long teamId, Team team) {
        Optional<Team> teamOptional = teamRepo.findById(teamId);
        if (team == null || !teamOptional.isPresent()) {
            throw new RuntimeException("Team not found");
        }
        teamRepo.save(team);
        return "Team updated successfully";
    }

    public String deleteTeam(Long teamId) {
        teamRepo.deleteById(teamId);
        return "Team deleted successfully";
    }


    public String addTeamToDepartment(Long depId , Long projectId , Team team , DepartmentService departmentService , ProjectService projectService){

        Optional<Team> existingTeam = teamRepo.findById(team.getTeamId());

        if(existingTeam.isPresent()){
            team = existingTeam.get();
        }

        Department dep = departmentService.getDepartmentById(depId);
        Project proj = null;

        //assigning project is not mandatory
        try {
             proj = projectService.getProjectById(projectId);
        }
        catch (RuntimeException runtimeException){

        }

        if(proj!=null){
            team.setProject(proj);
        }

        if(proj != null && !proj.getDepartment().getDepartmentId().equals(depId)){
            throw new RuntimeException("Project "+proj.getProjectName()+" is not assigned to dep: "+dep.getDepName());
        }
        team.setDepartment(dep);


        teamRepo.save(team);

        if (dep!=null && proj !=null) {
            return "Team "+team.getTeamName() + "assigned to dep: "+ dep.getDepName() +"and project: " + proj.getProjectName() +  " successfully";
        }
        if(dep!=null){
            return "Team "+team.getTeamName() + "assigned to dep: "+ dep.getDepName() + " successfully";
        }
        return "Team "+team.getTeamName() + " successfully";
    }

    public String addTeamToDepartment(String depName , String projectName , Team team , DepartmentService departmentService , ProjectService projectService){

        if(team.getTeamId()!=null) {
            Optional<Team> existingTeam = teamRepo.findById(team.getTeamId());


            if (existingTeam.isPresent()) {
                team = existingTeam.get();
            }

        }

        Department dep = departmentService.getDepartmentByName(depName);
        Project proj = null;
        try {
            proj = projectService.getByProjectName(projectName);
        }catch (RuntimeException runtimeException){

        }

        if(proj != null && !proj.getDepartment().getDepName().equals(depName)){
            throw new RuntimeException("Project "+proj.getProjectName()+" is not assigned to dep: "+dep.getDepName());
        }

        team.setDepartment(dep);
        if(proj!=null){
            team.setProject(proj);
        }

        teamRepo.save(team);

        if (dep!=null && proj !=null) {
            return "Team "+team.getTeamName() + "assigned to dep: "+ dep.getDepName() +"and project: " + proj.getProjectName() +  " successfully";
        }
        if(dep!=null){
            return "Team "+team.getTeamName() + "assigned to dep: "+ dep.getDepName() + " successfully";
        }
        return "Team "+team.getTeamName() + " successfully";
    }

    public List<Team> v(){
        return teamRepo.findAll();
    }

    public List<Team> findAllFiltered(String depName, String projectName){
        System.out.println("findAllFiltered");
        depName=depName==null?null:"%"+depName+"%";
        projectName=projectName==null?null:"%"+projectName+"%";

        System.out.println(depName+" "+projectName);
        return teamRepo.findAllFiltered(depName, projectName);
    }

    public Employee getTeamLead(Long teamId){
        Optional<Team> v =teamRepo.findById(teamId);

        if(v.isEmpty()){
            throw  new RuntimeException("Team not found");
        }
        Team team = v.get();

        if(team.getTechLead()!=null){
            return  team.getTechLead();
        }
        throw   new RuntimeException("Team "+team.getTeamName()+ " doesn't have technical lead.");
    }



}