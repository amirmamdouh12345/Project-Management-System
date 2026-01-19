package com.projectManagement.demo.Services.Structure;

import com.projectManagement.demo.DTOs.Entities.EmployeeDTO;
import com.projectManagement.demo.Entities.CompositeKeys.SalaryStrategyId;
import com.projectManagement.demo.Entities.SalaryStrategy;
import com.projectManagement.demo.Entities.Structure.Department;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Team;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import com.projectManagement.demo.Repos.DepartmentRepo;
import com.projectManagement.demo.Repos.EmployeeRepo;
import com.projectManagement.demo.Repos.SalaryStrategyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private TeamService teamService;

    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private SalaryStrategyRepo salaryStrategyRepo;


    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public List<Employee> getAllActivatedEmployees() {
        return employeeRepo.findAllActivated();
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> emp = employeeRepo.findById(id);
        return emp.orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee checkIfEmployeeIsHr(Long id) {
        Optional<Employee> emp = employeeRepo.findEmployeeRoleById(id, JobTitle.HR);
        return emp.orElseThrow(() -> new RuntimeException("No HR Employee with this Id found"));
    }


    public Employee getEmployeeByName(String firstName , String lastName) {
        Optional<Employee> emp = employeeRepo.findByName(firstName,lastName);
        return emp.orElseThrow(() -> new RuntimeException("Employee not found"));
    }



//    public Employee getEmployeeByEmail(String email) {
//        Optional<Employee> emp = employeeRepo.findByEmail(email);
//        return emp.orElseGet(() -> {
//            throw new RuntimeException("Employee not found");
//        });
//    }

    public String addEmployee(EmployeeDTO emp) {
        Employee newEmp = new Employee();
        String firstName =emp.getFirstName();
        String lastName =emp.getLastName();



        // if id exists --> update not insert
        if(emp.getEmployeeId() != null){
            Optional<Employee> isExpExists = employeeRepo.findById(emp.getEmployeeId());

            if(isExpExists.isPresent()){
                newEmp = isExpExists.get();

            }else{
                throw new RuntimeException("Employee Id not found");
            }
        }
        //insert
        else {
            Optional<Employee> isEmpExists= employeeRepo.findByName(firstName,lastName);

            //name already exists? --> exception
            if(isEmpExists.isPresent()){
                throw new RuntimeException("Employee Name already exists");
            }

            newEmp.setCreatedAt(Timestamp.from(Instant.now()));
            newEmp.setVacationBalance(calculateVacationBalanceBasedOnCurrentDay());
            newEmp.setSickLeaveBalance(calculateSickLeaveBalanceBasedOnCurrentDay());
        }

        Long departmentId = emp.getDepartmentId();

        if(departmentId==null){
            throw new RuntimeException("Department Id is mandatory");
        }

        //validate team with department
        String teamName = emp.getTeamName();
        Long teamId = emp.getTeamId();
        if(teamId != null){
            //find team by id and department
            Team assignedTeam = teamService.getTeamByTeamIdAndDepartment(teamId,departmentId);
            newEmp.setTeam(assignedTeam);
            newEmp.setDepartment(assignedTeam.getDepartment());

            System.out.println("after teamId");

        }else if(teamName!=null){
            //find team by name and department
            System.out.println("before get teamName");
            Team assignedTeam = teamService.getTeamByTeamNameAndDepartment(teamName,departmentId);
            System.out.println("after get teamName");

            newEmp.setTeam(assignedTeam);
            newEmp.setDepartment(assignedTeam.getDepartment());
        }else {
            // no team assigned  (optional)  ,   but department is mandatory
            Department assignedDepartment = departmentRepo
                    .findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            newEmp.setDepartment(assignedDepartment);
        }

        EmployeeRole role = emp.getEmployeeRole();
        System.out.println(newEmp);
        if(role==EmployeeRole.MANAGER){
            Department dep =newEmp.getDepartment();
            int count = employeeRepo.countManagersInDepById(dep.getDepartmentId());
            if(count >0){
                throw new RuntimeException("There's already an assigned active Manager to Department "+dep.getDepName());
            }
            newEmp.setTeam(null);
            dep.setDepManager(newEmp);
        }else if (role==EmployeeRole.TECHLEAD){
            System.out.println("is role techlead");

            int count = employeeRepo.countTechLeadInTeamById(newEmp.getTeam().getTeamId());
            if(count >0){
                throw new RuntimeException("There's already an assigned Technical Leader to Team "+newEmp.getTeam().getTeamName());
            }

            Team team = newEmp.getTeam();
            if(team!=null){
                team.setTechLead(newEmp);
            }
            System.out.println("after is role techlead");

        }



        newEmp.setFirstName(emp.getFirstName()!=null?emp.getFirstName():newEmp.getFirstName());
        newEmp.setLastName(emp.getLastName()!=null?emp.getLastName():newEmp.getLastName());
        newEmp.setActive(newEmp.getActive()!=null? newEmp.getActive():false);
        newEmp.setEmail(emp.getEmail()!=null?emp.getEmail(): newEmp.getEmail());
        newEmp.setJobTitle(emp.getJobTitle()!=null?emp.getJobTitle(): newEmp.getJobTitle());
        newEmp.setEmployeeRole(emp.getEmployeeRole()!=null?emp.getEmployeeRole(): newEmp.getEmployeeRole());
        System.out.println(newEmp);

        //set salary strategy
        SalaryStrategyId salaryStrategyId = new SalaryStrategyId();
        salaryStrategyId.setJobTitle(emp.getJobTitle());
        salaryStrategyId.setEmployeeRole(emp.getEmployeeRole());

        SalaryStrategy salaryStrategy = salaryStrategyRepo.
                findBySalaryStrategyIdJobTitleAndSalaryStrategyIdEmployeeRole(
                        salaryStrategyId.getJobTitle(),
                        salaryStrategyId.getEmployeeRole())
                .orElseThrow(() -> new RuntimeException("Salary strategy not found"));
        newEmp.setConstantSalary(salaryStrategy.getBasicSalary());
        employeeRepo.save(newEmp);

        return emp.getEmployeeId()!=null? "Employee updated Successfully": "Employee added successfully";
    }

//    public String updateEmployee(Long empId, Employee emp) {
//        Optional<Employee> empOptional = employeeRepo.findById(empId);
//        if (emp == null || !empOptional.isPresent()) {
//            throw new RuntimeException("Employee not found");
//        }
//        employeeRepo.save(emp);
//        return "Employee updated successfully";
//    }


    public int calculateVacationBalanceBasedOnCurrentDay(){
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        int day =currentDate.getDate();

        //first week -> 4  , second week -> 3 , third week -> 2 , fourth week -> 1
        return day<=7?4:day<=14?3:day<=21?2:1;
    }

    public int calculateSickLeaveBalanceBasedOnCurrentDay(){
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        int day =currentDate.getDate();

        //first week -> 4  , second week -> 3 , third week -> 2 , fourth week -> 1
        return day<=14?2:1;
    }

    public void saveEmployee(Employee employee){
        employeeRepo.save(employee);
    }

    public String deleteEmployee(Long empId) {
        employeeRepo.deleteById(empId);
        return "Employee deleted successfully";
    }

    public List<Employee> getEmployeesByTeamName(String teamName) {
        return employeeRepo.getEmployeesByTeamName(teamName);
    }

    public String activateEmployee(Long empId){
        Optional<Employee> optionalEmployee = employeeRepo.findDeactivatedEmployee(empId);
        if(optionalEmployee.isPresent()){
            Employee emp = optionalEmployee.get();
            emp.setActive(true);
            emp.setDeactivationTime(null);

            employeeRepo.save(emp);
        }else{
            throw new RuntimeException("No Deactivated Employees in this Id.");
        }
        return  "Employee activated successfully";
    }

    public String deactivateEmployee(Long empId){
        Optional<Employee> optionalEmployee = employeeRepo.findActivatedEmployee(empId);
        if(optionalEmployee.isPresent()){
            Employee emp = optionalEmployee.get();
            emp.setActive(false);
            emp.setDeactivationTime(Timestamp.from(Instant.now()));
            employeeRepo.save(emp);
        }else{
            throw new RuntimeException("No Activated Employees in this Id.");
        }
        return  "Employee Deactivated successfully";
    }

    public String assignEmployeeToNewRole(EmployeeDTO employeeDTO){
        Optional<Employee> optionalEmployee =employeeRepo.findById(employeeDTO.getEmployeeId());

        if (optionalEmployee.isEmpty()){
            throw new RuntimeException("Employee not found");

        }
        Employee employee = optionalEmployee.get();
        EmployeeRole employeeRole = employeeDTO.getEmployeeRole();


        if(employee.getEmployeeRole() == employeeDTO.getEmployeeRole()){
            return "Employee is already assigned to "+employeeRole.getRole()+" role.";
        }




        if(employeeRole==EmployeeRole.MANAGER){
            Department dep = employee.getTeam().getDepartment();
            int count =  employeeRepo.countManagersInDepById(dep.getDepartmentId());
            if(count>0){
                throw new RuntimeException("There's already an assigned active Manager to " +dep.getDepName()  +" department.");
            }
            dep.setDepManager(employee);

        }



        if(employeeRole==EmployeeRole.TECHLEAD){
            Team team = employee.getTeam();
            int count =  employeeRepo.countTechLeadInTeamById(team.getTeamId());
            if(count>0){
                throw new RuntimeException("There's already an assigned TechLead to " +employee.getTeam().getTeamName()+" team.");
            }
            team.setTechLead(employee);
        }

        if(employee.getEmployeeRole() == EmployeeRole.MANAGER){
            employee.getTeam().getDepartment().setDepManager(null);
        }
        else if(employee.getEmployeeRole() == EmployeeRole.TECHLEAD){
            employee.getTeam().setTechLead(null);
        }




        employee.setEmployeeRole(employeeDTO.getEmployeeRole());



        employeeRepo.save(employee);

        return  "Employee assigned as "+ employee.getEmployeeRole().getRole() +" successfully";
    }


    public String requestForVacation(Long empId){

        return "Vacation Requested Successfully";
    }


    public Employee getEmployeeBalance(Long empId){
        System.out.println("getEmployeeBalance");
        return employeeRepo.findActivatedEmployee(empId)
                .orElseThrow(
                        ()-> new RuntimeException("Employee not found")
                );
    }


}