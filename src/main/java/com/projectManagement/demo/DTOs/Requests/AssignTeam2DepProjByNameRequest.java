package com.projectManagement.demo.DTOs.Requests;

import com.projectManagement.demo.Entities.Structure.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTeam2DepProjByNameRequest {
    private String depName;
    private String projName;
    private Team team;  // existing or new
}
