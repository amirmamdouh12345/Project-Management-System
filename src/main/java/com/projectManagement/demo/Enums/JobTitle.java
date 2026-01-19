package com.projectManagement.demo.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum JobTitle {
    Backend("Backend"),
    Frontend("Frontend"),
    Devops("Devops"),
    AI("AI"),
    UIUX("UIUX"),
    BusinessAnalyst("BusinessAnalyst"),
    HR("HR");

    String title;
}
