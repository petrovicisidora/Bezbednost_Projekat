package com.main.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEmployeeToProjectDto {
    private KorisnikDto employee;
    private ProjectDto project;
    private String jobDescription;
}
