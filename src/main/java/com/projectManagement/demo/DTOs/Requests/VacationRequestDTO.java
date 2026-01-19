package com.projectManagement.demo.DTOs.Requests;

import com.projectManagement.demo.Convernters.LocalDateConverter;
import com.projectManagement.demo.Enums.VacationType;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
public class VacationRequestDTO {

    Long employeeId;

    String reason;

    @Convert(converter = LocalDateConverter.class)
    LocalDate startDate;

    @Convert(converter = LocalDateConverter.class)
    LocalDate endDate;

    @Enumerated(EnumType.STRING)
    VacationType vacationType;

    @Override
    public String toString() {
        return "VacationRequestDTO{" +
                "employeeId=" + employeeId +
                ", reason='" + reason + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", vacationType=" + vacationType +
                '}';
    }
}
