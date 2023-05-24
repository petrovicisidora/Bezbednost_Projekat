package com.main.app.service;

import com.main.app.domain.dto.EmployeeInProjectDto;
import com.main.app.domain.dto.ProjectDto;
import com.main.app.domain.model.EmployeeInProject;
import com.main.app.domain.model.Korisnik;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeInProjectService {

    void addEmployeesToProject(EmployeeInProjectDto employeeInProjectDto);
    Optional<Korisnik> getKorisnikById(Long employeeId);
    List<EmployeeInProjectDto> getEmployeesInProject(Long projectId);
    List<EmployeeInProjectDto> getAllEmployeesInProjects();
    public List<ProjectDto> getProjectsByEmployee(String email);

    void updateJobDescription(Long employeeInProjectId, String newJobDescription);

    Optional<EmployeeInProject> getEmployeeInProjectById(Long employeeInProjectId);

}
