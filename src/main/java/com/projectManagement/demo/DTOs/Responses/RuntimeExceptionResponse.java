package com.projectManagement.demo.DTOs.Responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuntimeExceptionResponse {

    public Integer statusCode;

    public String Description;

    public String timeStamp;

}
