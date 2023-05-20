package com.main.app.controller;

import com.main.app.domain.dto.EmployeeInProjectDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.service.EmployeeInProjectService;
import com.main.app.service.KorisnikService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EmployeeInProjectDto>> getAllEmployeesInProjects() {
        List<EmployeeInProjectDto> employeesInProjects = employeeInProjectService.getAllEmployeesInProjects();
        return ResponseEntity.ok(employeesInProjects);
    }
}
