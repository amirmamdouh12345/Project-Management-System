package com.projectManagement.demo.Enums;

public enum TaskStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS") ,
    DONE("DONE"),
    CANCELLED("CANCELLED"),

    ;
    String status;

    TaskStatus(String status) {
    this.status=status;
    }
}
