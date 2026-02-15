package com.projectManagement.demo.Services.Structure;

import com.projectManagement.demo.DTOs.Entities.TaskDTO;
import com.projectManagement.demo.DTOs.Requests.AssignNewTaskToEmployee;
import com.projectManagement.demo.DTOs.Requests.SearchFilters.TaskSearchFilterRequest;
import com.projectManagement.demo.Entities.Structure.Employee;
import com.projectManagement.demo.Entities.Structure.Task;
import com.projectManagement.demo.Enums.TaskStatus;
import com.projectManagement.demo.Repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private EmployeeService employeeService;

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public List<Task> getAllTasksFiltered(TaskSearchFilterRequest req) {
        String[] fullName = req.getEmpName().trim().split("\\s+");
        String firstName = "";
        String lastName = "";
        int countSpaces = fullName.length-1;
        if(fullName.length==1){
            firstName= "%"+fullName[0]+"%";
            lastName ="%"+fullName[0]+"%";
        }else{
            firstName= "%"+fullName[0]+"%";
            lastName= "%"+fullName[1]+"%";
        }


        req.setProjectName(req.getProjectName()!=null?"%"+req.getProjectName()+"%":null);
        req.setTeamName(req.getTeamName()!=null?"%"+req.getTeamName()+"%":null);

        System.out.println(firstName+" "+lastName+" "+req.getProjectName()+" "+req.getTeamName()+" "+req.getTaskStatus());
        return taskRepo.findAllFilteredTasks(firstName,lastName,req.getProjectName(),req.getTeamName(),req.getTaskStatus(),countSpaces);
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepo.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public String assignTaskToEmployee(AssignNewTaskToEmployee newTask) {
        TaskDTO taskDto = newTask.getTask();
        Long taskId = taskDto.getTaskId();
        Task task=new Task();
        // new to update not create
        if(taskId != null) {
            Optional<Task> optionalTask = taskRepo.findById(taskId);
            if(optionalTask.isPresent()) {
                task = optionalTask.get();
                task.setUpdatedAt(Timestamp.from(Instant.now()));

            }else {
                throw new RuntimeException("Task you are trying to update is not found");
            }
        }else {
            task.setCreatedAt(Timestamp.from(Instant.now()));
        }

        String fullNameEmp = newTask.getFullName();
        String[] v = fullNameEmp.split(" ");

        //included exception handling
        Employee emp = employeeService.getEmployeeByName(v[0],v[1]).orElseThrow();

        task.setEmployee(emp);
        task.setProject(emp.getTeam().getProject());
        task.setTeam(emp.getTeam());

        task.setTaskStatus(newTask.getTask().getTaskStatus() != null ?
                newTask.getTask().getTaskStatus():task.getTaskStatus()!=null ? task.getTaskStatus(): TaskStatus.NEW );
        task.setDescription(newTask.getTask().getDescription()!=null?newTask.getTask().getDescription():task.getDescription());


        Task saved = taskRepo.save(task);
        if (saved == null) {
            throw new RuntimeException("Task not added");
        }
        return "Task added successfully";
    }


    public List<Task> getTasksByEmployeeName(String firstName , String lastName, TaskStatus taskStatus){
        return taskRepo.findByEmpName(firstName,lastName,taskStatus.name());
    }

    public List<Task> getTasksByEmployeeName(String firstName , String lastName){
        return taskRepo.findByEmpName(firstName,lastName);
    }

//
//    public String updateTask(Long taskId, Task task) {
//        Optional<Task> taskOptional = taskRepo.findById(taskId);
//        if (task == null || !taskOptional.isPresent()) {
//            throw new RuntimeException("Task not found");
//        }
//        taskRepo.save(task);
//        return "Task updated successfully";
//    }

    public String deleteTask(Long taskId) {
        taskRepo.deleteById(taskId);
        return "Task deleted successfully";
    }
}