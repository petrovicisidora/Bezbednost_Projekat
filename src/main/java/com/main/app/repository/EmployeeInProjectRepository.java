package com.main.app.repository;

import com.main.app.domain.model.EmployeeInProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeInProjectRepository extends JpaRepository<EmployeeInProject, Long> {
    List<EmployeeInProject> findByProjectId(Long projectId);
    List<EmployeeInProject> findByWorkerId(Long employeeId);
    @Query("SELECT e.id FROM EmployeeInProject e WHERE e.project.id = :projectId AND e.worker.id = :workerId")
    Long findEmployeeInProjectIdByProjectIdAndWorkerId(@Param("projectId") Long projectId, @Param("workerId") Long workerId);
}
