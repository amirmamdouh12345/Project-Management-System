package com.projectManagement.demo.Enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VacationType {
    ANNUAL("ANNUAL",null),
    SICK("SICK",null),
    WEDDING("WEDDING",3),
    FUNERAL("FUNERAL",2),
    PREGNANCY("PREGNANCY",10)
    ;

    String type;
    Integer noOfDays;


}
