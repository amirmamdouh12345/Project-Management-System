package com.projectManagement.demo.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum AttendanceStatus {
    NOT_ASSIGNED("NOT_ASSIGNED"),
    ATTENDED("ATTENDED"),
    ABSENT("ABSENT"),
    DEDUCTED("DEDUCTED"),
    OVERTIME("OVERTIME"),
    OFF("OFF");

    String AttendanceStatus;
}
