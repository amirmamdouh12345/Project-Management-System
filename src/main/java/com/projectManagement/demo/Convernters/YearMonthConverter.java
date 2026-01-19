package com.projectManagement.demo.Convernters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.annotation.Annotation;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, String> {


    @Override
    public String convertToDatabaseColumn(YearMonth yearMonth) {
        return yearMonth != null ? yearMonth.toString() : null; // "2026-01"
    }

    @Override
    public YearMonth convertToEntityAttribute(String s) {
        return s!=null?YearMonth.parse(s):null;
    }
}
