package com.main.app.repository;

import com.main.app.domain.model.EmployeeInProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeInProjectRepository extends JpaRepository<EmployeeInProject, Long> {
    List<EmployeeInProject> findByProjectId(Long projectId);
}
