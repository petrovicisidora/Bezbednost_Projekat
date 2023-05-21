package com.main.app.service;

import com.main.app.domain.dto.ProjectDto;
import com.main.app.domain.model.Project;
import com.main.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return mapProjectsToProjectDtos(projects);
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = mapProjectDtoToProject(projectDto);
        Project createdProject = projectRepository.save(project);
        return mapProjectToProjectDto(createdProject);
    }

    private List<ProjectDto> mapProjectsToProjectDtos(List<Project> projects) {
        return projects.stream()
                .map(this::mapProjectToProjectDto)
                .collect(Collectors.toList());
    }

    private ProjectDto mapProjectToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        return projectDto;
    }

    private Project mapProjectDtoToProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        return project;
    }

    @Override
    public ProjectDto getProjectByName(String projectName) {
        Project project = projectRepository.findByName(projectName);
        if (project != null) {
            return mapProjectToProjectDto(project);
        } else {
            throw new EntityNotFoundException("Project not found");
        }
    }


    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        // Mapirajte ostale atribute projekta na projektDto

        return projectDto;
    }

}
