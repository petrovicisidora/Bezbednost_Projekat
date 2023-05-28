package com.main.app.controller;

import com.main.app.domain.dto.ProjectDto;
import com.main.app.domain.model.Project;
import com.main.app.service.KorisnikService;
import com.main.app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/projekat")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/getAll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENADZER_PROJEKTA')")

    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = projectService.createProject(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
    @GetMapping("/findByProjectName/{projectName}")
    public ResponseEntity<ProjectDto> getProjectByName(@PathVariable String projectName) {
        try {
            ProjectDto projectDto = projectService.getProjectByName(projectName);
            return ResponseEntity.ok(projectDto);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        try {
            ProjectDto projectDto = projectService.getProjectById(id);
            return ResponseEntity.ok(projectDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
