package com.main.app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_in_project")
public class EmployeeInProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Korisnik worker;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Project project;

    private String job_description;

    private LocalDate jobStartTime;

    private LocalDate jobEndTime;
}
