package com.projectManagement.demo.Services.Structure;

import com.projectManagement.demo.DTOs.Entities.EmployeeDTO;
import com.projectManagement.demo.Entities.CompositeKeys.SalaryStrategyId;
import com.projectManagement.demo.Entities.SalaryStrategy;
import com.projectManagement.demo.Entities.Structure.Department;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Team;
import com.projectManagement.demo.Entities.VacationRequests.VacationRequest;
import com.projectManagement.demo.Enums.EmployeeRole;
import com.projectManagement.demo.Enums.JobTitle;
import com.projectManagement.demo.JWT.JwtService;
import com.projectManagement.demo.KafkaMessages.ActiveAccountKafkaMailMessage;
import com.projectManagement.demo.Repos.DepartmentRepo;
import com.projectManagement.demo.Repos.EmployeeRepo;
import com.projectManagement.demo.Repos.SalaryStrategyRepo;
import com.projectManagement.demo.Repos.VacationRequestRepo;
import com.projectManagement.demo.Services.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Iterator;
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

    @Autowired
    private VacationRequestRepo vacationRequestRepo;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private JwtService jwtService;

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


    public Optional<Employee> getEmployeeByName(String firstName , String lastName) {
        Optional<Employee> emp = employeeRepo.findByName(firstName,lastName);
        return emp;
    }



//    public Employee getEmployeeByEmail(String email) {
//        Optional<Employee> emp = employeeRepo.findByEmail(email);
//        return emp.orElseGet(() -> {
//            throw new RuntimeException("Employee not found");
//        });
//    }

    public String updateEmployee(EmployeeDTO employeeDTO){
        Employee employee = employeeRepo.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Team team = null;
        Department department = null;

        if(employeeDTO.getDepartmentId()!=null){
             department = departmentRepo.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        }else{
            department = employee.getDepartment();
        }

        // if team id is -1 --> unassign from all teams
        if(employeeDTO.getTeamId()!=null && employeeDTO.getTeamId() != -1){
            team = teamService
                    .getTeamByTeamIdAndDepartment(
                            employeeDTO.getTeamId(),
                            department.getDepartmentId()!=null?department.getDepartmentId():employee.getDepartment().getDepartmentId());
        }else if (employeeDTO.getTeamId()!=null ){ // if team id is -1 --> do nothing --> team = null
            //do nothing  --> team = null
        }else{
            team = employee.getTeam();
        }

        assignExistingEmployeeToNewRole(employee,department,team,employeeDTO.getEmployeeRole());

        employee.setDepartment(department);
        employee.setTeam(team);

        employee.setEmail(employeeDTO.getEmail()!=null?employeeDTO.getEmail():employee.getEmail());
        employee.setJobTitle(employeeDTO.getJobTitle()!=null?employeeDTO.getJobTitle():employee.getJobTitle());
        employee.setFirstName(employeeDTO.getFirstName()!=null?employeeDTO.getFirstName():employee.getFirstName());
        employee.setLastName(employeeDTO.getLastName()!=null?employeeDTO.getLastName():employee.getLastName());

        employeeRepo.save(employee);
        return "Employee updated successfully";
    }


    public String addNewEmployee(EmployeeDTO emp) {
        Employee newEmp = new Employee();
        String firstName =emp.getFirstName();
        String lastName =emp.getLastName();

        Department department = null;
        Team team = null;

        if(firstName == null || lastName == null){
            throw new RuntimeException("First name and last name are mandatory");
        }
        else {
            Optional<Employee> existingEmp = getEmployeeByName(firstName,lastName);



            //validate name
            if(existingEmp.isPresent()){
                throw new RuntimeException("Employee already exists");
            }
        }

        if(emp.getDepartmentId() == null){
            throw new RuntimeException("Department Id is mandatory");
        }else {
            //assign department ( mandatory )
            department = departmentRepo.findById(emp.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            newEmp.setDepartment(department);
        }
        //assign team ( optional )
        if(emp.getTeamId() != null){
            team = teamService.getTeamByTeamIdAndDepartment(emp.getTeamId(),department.getDepartmentId());
            newEmp.setTeam(team);
        }else if(emp.getTeamName()!=null){
            team = teamService.getTeamByTeamNameAndDepartment(emp.getTeamName(),department.getDepartmentId());
            newEmp.setTeam(team);
        }else {
            newEmp.setTeam(null);
        }


        newEmp.setCreatedAt(Timestamp.from(Instant.now()));
        newEmp.setVacationBalance(calculateVacationBalanceBasedOnCurrentDay());
        newEmp.setSickLeaveBalance(calculateSickLeaveBalanceBasedOnCurrentDay());

        System.out.println("before assign");
        // why assign them as null? because they assigned in newEmp itself.
        assignExistingEmployeeToNewRole(newEmp,null,null,emp.getEmployeeRole());
        System.out.println("after assign");


        newEmp.setFirstName(emp.getFirstName()!=null?emp.getFirstName():newEmp.getFirstName());

        newEmp.setLastName(emp.getLastName()!=null?emp.getLastName():newEmp.getLastName());

        newEmp.setActive(false);

        newEmp.setEmail(emp.getEmail()!=null?emp.getEmail(): newEmp.getEmail());

        newEmp.setJobTitle(emp.getJobTitle()!=null?emp.getJobTitle(): newEmp.getJobTitle());

        newEmp.setEmployeeRole(emp.getEmployeeRole()!=null?emp.getEmployeeRole(): newEmp.getEmployeeRole());
        System.out.println(newEmp);


        //set salary strategy id
        SalaryStrategyId salaryStrategyId = new SalaryStrategyId();
        salaryStrategyId.setJobTitle(emp.getJobTitle());
        salaryStrategyId.setEmployeeRole(emp.getEmployeeRole());

        updateEmployeeSalary(newEmp, salaryStrategyId);


        employeeRepo.save(newEmp);

        return "Employee added successfully";
    }


    private void updateEmployeeSalary(Employee emp,SalaryStrategyId salaryStrategyId){

        //get salary strategy
        SalaryStrategy salaryStrategy = salaryStrategyRepo.
                findBySalaryStrategyIdJobTitleAndSalaryStrategyIdEmployeeRole(
                        salaryStrategyId.getJobTitle(),
                        salaryStrategyId.getEmployeeRole())
                .orElseThrow(() -> new RuntimeException("Salary strategy not found"));

        //set salary to employee
        emp.setConstantSalary(salaryStrategy.getBasicSalary());

    }
    public int calculateVacationBalanceBasedOnCurrentDay(){
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        int day =currentDate.getDay();

        //first week -> 4  , second week -> 3 , third week -> 2 , fourth week -> 1
        return day<=7?4:day<=14?3:day<=21?2:1;
    }

    public int calculateSickLeaveBalanceBasedOnCurrentDay(){
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        int day =currentDate.getDay();

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

    public String activateEmployeeRequest(Long empId){
        Optional<Employee> optionalEmployee = employeeRepo.findDeactivatedEmployee(empId);
        if(optionalEmployee.isPresent()){



            ActiveAccountKafkaMailMessage message = new ActiveAccountKafkaMailMessage();
            message.setActivationLink("www.amir.com"); //TODO: JWT remains
            message.setMinutes(60);
            message.setSubject("Activate your account");
            message.setEmpId(empId.intValue());
//            message.setToEmail(optionalEmployee.get().getEmail());
            message.setToEmail("amirmamdouhaws@gmail.com");

            message.setUsername(optionalEmployee.get().getFirstName());
            System.out.println("message is sending to amirmamdouhaws@gmail.com");
            kafkaProducerService.sendMail(message);
        }else{
            throw new RuntimeException("No Deactivated Employees in this Id.");
        }
        return  "Employee activated successfully";
    }


    public String activateEmployee(String token){

        //validate expiry time
        boolean isExpired = jwtService.isTokenExpired(token);
        if(isExpired){
            throw new RuntimeException("Token is expired");
        }

        Long empId = jwtService.extractEmpId(token);
        System.out.println("empId: "+empId);
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

    //base assign role
    @Transactional
    public String assignExistingEmployeeToNewRole(Employee employee , Department dep , Team teamArg , EmployeeRole employeeRole) {
        assert employee != null;

        boolean isInAnotherDepartment = false;
        boolean isInAnotherTeam = false;

        if(employee.getDepartment()!= dep){
            isInAnotherDepartment = true;
        }
        if(employee.getTeam()!= teamArg){
            isInAnotherTeam = true;
        }

        //get department ( mandatory )
        Department department = dep!=null?dep:employee.getDepartment();

        //get team ( optional )
        Team team = teamArg!=null?teamArg:employee.getTeam();

        assert department != null;

        //new role
        if(employeeRole==EmployeeRole.MANAGER){
            int count =  employeeRepo.countManagersInDepById(department.getDepartmentId());
            if(count>0){
                throw new RuntimeException("There's already an assigned Manager to " +department.getDepName()  +" department.");
            }
            department.setDepManager(employee);
            team = null;

            // need to get all vacation requests related to department --> assign to the new department id
            List<VacationRequest> vacationRequests = vacationRequestRepo
                    .findAllVacationRequestsRelatedToDepartmentId(department.getDepartmentId());

            // all vacation records related to department itself
            Iterator<VacationRequest> iter= vacationRequests.iterator();

            while(iter.hasNext()){
                VacationRequest v = iter.next();
                v.setDepartmentManager(employee);
                vacationRequestRepo.save(v);
            }

        }
        else if(employeeRole==EmployeeRole.TECHLEAD){
            assert team != null;
            int count =  employeeRepo.countTechLeadInTeamById(team.getTeamId());
            if(count>0){
                throw new RuntimeException("There's already an assigned TechLead to " +employee.getTeam().getTeamName()+" team.");
            }


            System.out.println("before save");
            team.setTechLead(employee);
            System.out.println("after save");


            // need to get all vacation requests related to team --> assign to the new tech lead
            List<VacationRequest> vacationRequests = vacationRequestRepo
                    .findAllVacationRequestsRelatedToTeamId(team.getTeamId());

            // if he was previously related to a vacation records
            Iterator<VacationRequest> iter= vacationRequests.iterator();



            while(iter.hasNext()){
                VacationRequest v = iter.next();
                v.setTeamLead(employee);
                vacationRequestRepo.save(v);
            }

        }

        //previous role
        if(isInAnotherDepartment && employee.getEmployeeRole() == EmployeeRole.MANAGER){

            //previous dep --> techLead depManagerId should be null

            // if he was previously related to a vacation records
            Iterator<VacationRequest> iter= employee.getManagedVacationRequests().iterator();

            while(iter.hasNext()){
                VacationRequest v = iter.next();
                v.setDepartmentManager(null);
                vacationRequestRepo.save(v);
            }

            employee.getDepartment().setDepManager(null);

        }
        else if(isInAnotherTeam && employee.getEmployeeRole() == EmployeeRole.TECHLEAD){
            //previous team --> techLead should be null

            // if he was previously related to a vacation records
            Iterator<VacationRequest> iter= employee.getTeamLeadVacationRequests().iterator();

            while(iter.hasNext()){
                VacationRequest v = iter.next();
                v.setTeamLead(null);
                vacationRequestRepo.save(v);
            }

            employee.getTeam().setTechLead(null);
        }

        System.out.println("check");
        //make new changes
        employee.setDepartment(department);
        employee.setTeam(team); // in manager case it will be null

        employee.setEmployeeRole(employeeRole);
        if(team!=null){
            System.out.println("before update team db");

            teamService.updateTeam(team.getTeamId(),team);
            System.out.println("after update team db");

        }
        employeeRepo.save(employee);

        return "OK";

    }

    public String assignExistingEmployeeToNewRole(EmployeeDTO employeeDTO){
        Employee employee = employeeRepo.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Team team =null;
        Department department =null;

        if(employeeDTO.getTeamId() != null){
            Optional<Team> teamOptional = teamService.getOptionalTeamById(employeeDTO.getTeamId());
            team = teamOptional.orElse(null);
        }

        if(employeeDTO.getDepartmentId() != null){
            Optional<Department> departmentOptional = departmentRepo.findById(employeeDTO.getDepartmentId());
            department = departmentOptional.orElse(null);
        }

        return assignExistingEmployeeToNewRole(employee, department, team, employeeDTO.getEmployeeRole());

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
