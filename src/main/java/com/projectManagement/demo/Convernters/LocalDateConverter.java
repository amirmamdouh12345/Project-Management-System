package com.projectManagement.demo.Convernters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<java.time.LocalDate, String> {
    @Override
    public String convertToDatabaseColumn(java.time.LocalDate date) {
        return date!=null?date.toString():null;
    }

    @Override
    public java.time.LocalDate convertToEntityAttribute(String s) {
        return s!=null? java.time.LocalDate.parse(s):null;
    }
}
