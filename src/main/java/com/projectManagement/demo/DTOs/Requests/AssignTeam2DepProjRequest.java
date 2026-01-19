package com.projectManagement.demo.DTOs.Requests;

import com.projectManagement.demo.Entities.Structure.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTeam2DepProjRequest {
    Long DepId;

    Long ProjId;

    //existing or new
    Team Team;
}
