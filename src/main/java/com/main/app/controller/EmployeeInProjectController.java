package com.main.app.controller;

import com.main.app.domain.dto.EmployeeInProjectDto;
import com.main.app.domain.dto.ProjectDto;
import com.main.app.domain.model.EmployeeInProject;
import com.main.app.domain.model.Korisnik;
import com.main.app.service.EmployeeInProjectService;
import com.main.app.service.KorisnikService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employeeproject")
public class EmployeeInProjectController {

    private final EmployeeInProjectService employeeInProjectService;
    private final KorisnikService korisnikService;

    public EmployeeInProjectController(EmployeeInProjectService employeeInProjectService, KorisnikService korisnikService) {
        this.employeeInProjectService = employeeInProjectService;
        this.korisnikService = korisnikService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<String> getEmployeeById(@PathVariable Long employeeId) {
        Optional<Korisnik> korisnikOptional = korisnikService.getKorisnikById(employeeId);
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            String fullName = korisnik.getFirstName() + " " + korisnik.getLastName();
            return ResponseEntity.ok(fullName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addEmployeeToProject")
    public ResponseEntity<Void> addEmployeesToProject(@RequestBody EmployeeInProjectDto employeeInProjectDto) {
        employeeInProjectService.addEmployeesToProject(employeeInProjectDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENADZER_PROJEKTA')")
    public ResponseEntity<List<EmployeeInProjectDto>> getAllEmployeesInProjects() {
        List<EmployeeInProjectDto> employeesInProjects = employeeInProjectService.getAllEmployeesInProjects();
        return ResponseEntity.ok(employeesInProjects);
    }

    @GetMapping("/employeesInProject/{projectId}")
    public ResponseEntity<List<EmployeeInProjectDto>> getEmployeesInProject(@PathVariable Long projectId) {
        List<EmployeeInProjectDto> employeesInProject = employeeInProjectService.getEmployeesInProject(projectId);
        return ResponseEntity.ok(employeesInProject);
    }

    @GetMapping("/getProjectsFromEmployee/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENADZER_PROJEKTA')")
    public ResponseEntity<List<ProjectDto>> getProjectsByEmployee(@PathVariable String email) {

        List<ProjectDto> projects = employeeInProjectService.getProjectsByEmployee(email);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/updateJobDescription/{employeeInProjectId}")
    public ResponseEntity<Void> updateJobDescription(@PathVariable Long employeeInProjectId, @RequestBody UpdateJobDescriptionRequest request) {
        String newJobDescription = request.getNewJobDescription();
        employeeInProjectService.updateJobDescription(employeeInProjectId, newJobDescription);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getById/{employeeInProjectId}")
    public ResponseEntity<EmployeeInProject> getEmployeeInProjectById(@PathVariable("employeeInProjectId") Long employeeInProjectId) {
        Optional<EmployeeInProject> employeeInProjectOptional = employeeInProjectService.getEmployeeInProjectById(employeeInProjectId);

        if (employeeInProjectOptional.isPresent()) {
            EmployeeInProject employeeInProject = employeeInProjectOptional.get();
            return ResponseEntity.ok(employeeInProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private EmployeeInProjectDto convertToEmployeeInProjectDto(EmployeeInProject employeeInProject) {
        EmployeeInProjectDto employeeInProjectDto = new EmployeeInProjectDto();
        employeeInProjectDto.setJobDescription(employeeInProject.getJob_description());
        // Postavite ostale vrednosti iz employeeInProject u employeeInProjectDto prema potrebi
        return employeeInProjectDto;
    }

    @GetMapping("/getEmployeeInProjectId/{projectId}/{workerId}")
    public ResponseEntity<Long> getEmployeeInProjectIdByProjectIdAndWorkerId(
            @PathVariable("projectId") Long projectId,
            @PathVariable("workerId") Long workerId) {
        Long employeeInProjectId = employeeInProjectService.getEmployeeInProjectIdByProjectIdAndWorkerId(projectId, workerId);
        if (employeeInProjectId != null) {
            return ResponseEntity.ok(employeeInProjectId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{employeeInProjectId}")
    public ResponseEntity<String> deleteEmployeeFromProject(@PathVariable Long employeeInProjectId) {
        try {
            employeeInProjectService.deleteEmployeeFromProject(employeeInProjectId);
            return ResponseEntity.ok("Employee removed from project successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove employee from project.");
        }
    }


}
