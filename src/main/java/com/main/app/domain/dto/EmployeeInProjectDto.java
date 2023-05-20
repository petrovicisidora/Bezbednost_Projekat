package com.main.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInProjectDto {
    private List<Long> employeeId;
    private Long projectId;
    private String jobDescription;
    private ProjectDto project;
    private String employeeFullName;
}
