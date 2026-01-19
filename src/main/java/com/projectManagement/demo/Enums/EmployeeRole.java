package com.projectManagement.demo.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EmployeeRole {
    MANAGER("MANAGER"),
    TECHLEAD("TECHLEAD"),
    SENIOR("SENIOR"),
    JUNIOR("JUNIOR"),
    INTERN("INTERN");

    String role;

}
