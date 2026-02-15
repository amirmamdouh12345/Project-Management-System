package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.Entities.EmployeeDTO;
import com.projectManagement.demo.DTOs.Requests.ActivateEmployeeRequest;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Services.Structure.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public String addEmployee(@RequestBody EmployeeDTO employee) {
        System.out.println("start");
        return employeeService.addNewEmployee(employee);
    }

    @PostMapping("/update")
    public String updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        System.out.println("start updating");
        return employeeService.updateEmployee(employeeDTO);
    }

    @PostMapping("/activate/request")
    public String activateRequest(@RequestBody EmployeeDTO employeeDTO){
        return employeeService.activateEmployeeRequest(employeeDTO.getEmployeeId());
    }


    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/byTeam")
    public List<Employee> getEmployeesByTeamName(@RequestParam String teamName) {
        return employeeService.getEmployeesByTeamName(teamName);
    }

    @PostMapping("/activate")
    public String activeEmployee(@RequestParam("token") String token) {
        System.out.println("activation");
        return employeeService.activateEmployee( token);
    }

    @PostMapping("/deactivate")
    public String deActiveEmployee(@RequestBody ActivateEmployeeRequest employeeRequest) {
        return employeeService.deactivateEmployee(employeeRequest.getEmpId());
    }



}