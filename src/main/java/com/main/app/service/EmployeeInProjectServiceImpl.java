package com.main.app.service;

import com.main.app.domain.dto.EmployeeInProjectDto;
import com.main.app.domain.dto.ProjectDto;
import com.main.app.domain.model.EmployeeInProject;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.Project;
import com.main.app.repository.EmployeeInProjectRepository;
import com.main.app.repository.KorisnikRepository;
import com.main.app.repository.ProjectRepository;
import org.hibernate.proxy.EntityNotFoundDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class EmployeeInProjectServiceImpl implements EmployeeInProjectService{

    private final EmployeeInProjectRepository employeeInProjectRepository;
    private final ProjectRepository projectRepository;
    private final KorisnikRepository korisnikRepository;

    private KorisnikService korisnikService;

    public EmployeeInProjectServiceImpl(EmployeeInProjectRepository employeeInProjectRepository,
                                        ProjectRepository projectRepository,
                                        KorisnikRepository korisnikRepository, KorisnikService korisnikService) {
        this.employeeInProjectRepository = employeeInProjectRepository;
        this.projectRepository = projectRepository;
        this.korisnikRepository = korisnikRepository;
        this.korisnikService = korisnikService;
    }

    @Override
    public void addEmployeesToProject(EmployeeInProjectDto employeeInProjectDto) {
        Project project = projectRepository.findById(employeeInProjectDto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project hasn't been found!"));
        List<Korisnik> korisnici = korisnikRepository.findAllById(employeeInProjectDto.getEmployeeId());
        String jobDescription = employeeInProjectDto.getJobDescription();

        for (Korisnik korisnik : korisnici) {
            EmployeeInProject employeeInProject = new EmployeeInProject();
            employeeInProject.setWorker(korisnik);
            employeeInProject.setProject(project);
            employeeInProject.setJobDescription(jobDescription); // Set the provided job description
            employeeInProjectRepository.save(employeeInProject);
        }
    }

        @Override
        public Optional<Korisnik> getKorisnikById(Long employeeId) {
            return korisnikService.getKorisnikById(employeeId);
        }

    public List<EmployeeInProjectDto> getAllEmployeesInProjects() {
        List<EmployeeInProject> employeeInProjects = employeeInProjectRepository.findAll();
        List<EmployeeInProjectDto> employeeInProjectDtos = convertToDtoList(employeeInProjects);
        return employeeInProjectDtos;
    }

    private List<EmployeeInProjectDto> convertToDtoList(List<EmployeeInProject> employeeInProjects) {
        List<EmployeeInProjectDto> employeeInProjectDtos = new ArrayList<>();

        for (EmployeeInProject employeeInProject : employeeInProjects) {
            EmployeeInProjectDto employeeInProjectDto = new EmployeeInProjectDto();
            employeeInProjectDto.setEmployeeId(Collections.singletonList(employeeInProject.getWorker().getId()));
            employeeInProjectDto.setProjectId(employeeInProject.getProject().getId());
            employeeInProjectDto.setJobDescription(employeeInProject.getJobDescription());

            // Retrieve the employee details and set them in the DTO
            Korisnik worker = employeeInProject.getWorker();
            String fullName = worker.getFirstName() + " " + worker.getLastName();
            employeeInProjectDto.setEmployeeFullName(fullName);

            // Retrieve the project details and set them in the DTO
            Project project = employeeInProject.getProject();
            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            projectDto.setStartDate(project.getStartDate());
            projectDto.setEndDate(project.getEndDate());
            employeeInProjectDto.setProject(projectDto);

            employeeInProjectDtos.add(employeeInProjectDto);
        }

        return employeeInProjectDtos;
    }

    @Override
    public List<ProjectDto> getProjectsByEmployee(String email) {
        Optional<Korisnik> korisnikOptional = korisnikService.getKorisnikByEmail(email);
        if (korisnikOptional.isPresent()) {
            List<EmployeeInProject> employeeInProjects = employeeInProjectRepository.findByWorkerId(korisnikOptional.get().getId());
            List<ProjectDto> projects = new ArrayList<>();

            for (EmployeeInProject employeeInProject : employeeInProjects) {
                ProjectDto projectDto = convertToProjectDto(employeeInProject.getProject());
                EmployeeInProjectDto employeeInProjectDto = convertToEmployeeInProjectDto(employeeInProject);
                employeeInProjectDto.setProject(projectDto);  // Set projectDto in employeeInProjectDto
                projects.add(projectDto);
            }

            return projects;
        } else {
            throw new EntityNotFoundException("Korisnik nije pronađen!");
        }
    }

    private EmployeeInProjectDto convertToEmployeeInProjectDto(EmployeeInProject employeeInProject) {
        EmployeeInProjectDto employeeInProjectDto = new EmployeeInProjectDto();
        employeeInProjectDto.setJobDescription(employeeInProject.getJobDescription());
        return employeeInProjectDto;
    }

    @Override
    public List<EmployeeInProjectDto> getEmployeesInProject(Long projectId) {
        List<EmployeeInProject> employeesInProject = employeeInProjectRepository.findByProjectId(projectId);
        List<EmployeeInProjectDto> employeeInProjectDtos = new ArrayList<>();

        for (EmployeeInProject employeeInProject : employeesInProject) {
            EmployeeInProjectDto employeeInProjectDto = new EmployeeInProjectDto();
            employeeInProjectDto.setEmployeeId(Collections.singletonList(employeeInProject.getWorker().getId()));
            employeeInProjectDto.setProjectId(employeeInProject.getProject().getId());
            employeeInProjectDto.setJobDescription(employeeInProject.getJobDescription());
            employeeInProjectDto.setProject(convertToProjectDto(employeeInProject.getProject()));
            employeeInProjectDto.setEmployeeFullName(getEmployeeFullName(employeeInProject.getWorker().getId()));
            employeeInProjectDtos.add(employeeInProjectDto);
        }

        return employeeInProjectDtos;
    }

    private ProjectDto convertToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        return projectDto;
    }

    private String getEmployeeFullName(Long employeeId) {
        Optional<Korisnik> korisnikOptional = korisnikService.getKorisnikById(employeeId);
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            return korisnik.getFirstName() + " " + korisnik.getLastName();
        } else {
            return "";
        }
    }






}
