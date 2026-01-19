package com.projectManagement.demo.Services.Structure;

import com.projectManagement.demo.DTOs.Entities.DepartmentDTO;
import com.projectManagement.demo.DTOs.Entities.ProjectDTO;
import com.projectManagement.demo.Entities.Structure.Department;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Project;
import com.projectManagement.demo.Repos.DepartmentRepo;
import com.projectManagement.demo.Repos.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ProjectService projectService;

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public Department getDepartmentById(Long id){

        Optional<Department> dep = departmentRepo.findById(id);

        return dep.orElseThrow(()-> new RuntimeException("Department not found"));
    }

    public Department getDepartmentByName(String name){

        Optional<Department> dep = departmentRepo.findByDepName(name);

        return dep.orElseGet(()-> {
            throw new RuntimeException("Department not found");
        });
    }

    //need privilege
    public String addDepartment(DepartmentDTO dep){

        Department department = new Department();

        if(departmentRepo.findByDepName(dep.getDepName()).isPresent()){
            throw new RuntimeException("Department already exists");
        };

        department.setDepName(dep.getDepName());
        department.setDescription(dep.getDescription());
        if (dep.getManagerId() != null) {
            Employee departmentManager = employeeRepo
                    .findById(dep.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            department.setDepManager(departmentManager);
        }

        if (dep.getProjects()!=null){

            for(ProjectDTO prjDto: dep.getProjects()){
                if (prjDto.getProjectId() != null) {
                    //update if project Id is not null
                    Project project = projectService.getProjectById(prjDto.getProjectId());
                    project.setDepartment(department);
                    project.setProjectName(prjDto.getProjectName()!=null?
                            prjDto.getProjectName() :
                            project.getProjectName());

                }else {
                    Project prj = new Project();
                    prj.setProjectName(prjDto.getProjectName());
                    prj.setDepartment(department);
                    prj.setProjectName(prjDto.getProjectName());
                    projectService.addProject(prj);
                }

            }

        }
        Department z = departmentRepo.save(department);
        if(z==null){
            throw new RuntimeException("Department not added");
        }
        return "Department added successfully";
    }

    //need privilege
    public String updateDepartment(Long depId,Department dep){
        Optional<Department> depOptional = departmentRepo.findById(depId);
        if(dep==null || dep.getDepartmentId().equals(depId)){
            throw new RuntimeException("Department not found");
        }
        Department z = departmentRepo.save(dep);
        if(z==null){
            throw new RuntimeException("Department not added");
        }
        return "Department added successfully";
    }

    public String deleteDepartment(Long depId){
        departmentRepo.deleteById(depId);
        return "Department deleted successfully";
    }








}
