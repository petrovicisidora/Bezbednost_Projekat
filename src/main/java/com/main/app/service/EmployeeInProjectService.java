package com.main.app.service;

import com.main.app.domain.dto.EmployeeInProjectDto;
import com.main.app.domain.model.EmployeeInProject;
import com.main.app.domain.model.Korisnik;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeInProjectService {

    void addEmployeesToProject(EmployeeInProjectDto employeeInProjectDto);
    Optional<Korisnik> getKorisnikById(Long employeeId);

    List<EmployeeInProjectDto> getAllEmployeesInProjects();
}
