package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.Requests.AllTasksForEmployeeRequest;
import com.projectManagement.demo.DTOs.Requests.AssignNewTaskToEmployee;
import com.projectManagement.demo.DTOs.Requests.SearchFilters.TaskSearchFilterRequest;
import com.projectManagement.demo.Entities.Structure.Task;
import com.projectManagement.demo.Services.Structure.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;



    @GetMapping()
    public List<Task> getAllTasksFiltered(){
        return taskService.getAllTasks();
    }


    @PostMapping("/filter")
    public List<Task> getAllTasksFiltered(@RequestBody(required = false) TaskSearchFilterRequest req) {
        System.out.println("filtered");
        // if there's no request body, return all tasks
        if (req == null) {
            return taskService.getAllTasks();
        }
        return taskService.getAllTasksFiltered(req);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public String assignTaskToEmployee(@RequestBody AssignNewTaskToEmployee task) {
        return taskService.assignTaskToEmployee(task);
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @PostMapping("/byEmp")
    public List<Task> getAllTasksForEmp(@RequestBody AllTasksForEmployeeRequest req){
        String[] name = req.getFullName().split(" ");
        String firstName = name[0];
        String lastName = name[1];

        if (req.getTaskStatus()!=null){
            return taskService.getTasksByEmployeeName(firstName,lastName,req.getTaskStatus());
        }
        return taskService.getTasksByEmployeeName(firstName,lastName);

    }


}