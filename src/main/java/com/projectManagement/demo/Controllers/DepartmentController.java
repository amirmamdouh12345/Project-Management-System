package com.projectManagement.demo.Controllers;

import com.projectManagement.demo.DTOs.Entities.DepartmentDTO;
import com.projectManagement.demo.Entities.Structure.Department;
import com.projectManagement.demo.Services.Structure.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @GetMapping("/name/{name}")
    public Department getDepartmentByName(@PathVariable String name) {
        return departmentService.getDepartmentByName(name);
    }

    @PostMapping
    public String addDepartment(@RequestBody DepartmentDTO department) {
        return departmentService.addDepartment(department);
    }

    @PutMapping("/{id}")
    public String updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department
    ) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }




}