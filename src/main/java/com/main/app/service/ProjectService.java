package com.main.app.service;

import com.main.app.domain.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getAllProjects();
    ProjectDto createProject(ProjectDto projectDto);
}
