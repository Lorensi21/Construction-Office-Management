package com.construction.app.service;
import com.construction.app.domain.*;
import com.construction.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class Construction {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRoleRepository projectRoleRepository;


    public Construction(ProjectRepository projectRepository, EmployeeRepository employeeRepository, ProjectRoleRepository projectRoleRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.projectRoleRepository = projectRoleRepository;
    }

    @Transactional(readOnly = true)
    public List<Project> loadAllProjectsInMemory() {
        return projectRepository.findAllWithRolesAndEmployees();
    }

    @Transactional(readOnly = true)
    public List<Employee> loadAllEmployees() {
        return employeeRepository.findAll();
    }


    @Transactional
    public Project createProject(String name, Double budget, ProjectCategory category, String extraDetail) {
        Project project = new Project();
        project.setName(name);
        project.setBudget(budget);
        project.setProjectCategory(category);

        if (category == ProjectCategory.BUSINESS) {
            project.setNameOfCompany(extraDetail);
        } else {
            try {
                project.setResidentCapacity(Integer.parseInt(extraDetail));
            } catch (NumberFormatException e) {
                project.setResidentCapacity(0);
            }
        }

        Customer defaultCustomer = new Customer();
        defaultCustomer.setCompanyOrPersonalName("Standard GUI Client");
        project.setCustomer(defaultCustomer);

        return projectRepository.save(project);
    }

    @Transactional
    public ProjectRole assignEmployee(Project project, Employee employee, String qualification, String description, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start Role Date cannot be after End Role Date!");
        }

        for (ProjectRole existingRole : project.getProjectRoles()) {
            if (existingRole.getEmployee().getId().equals(employee.getId()) &&
                    existingRole.getSpecificQualificationUsed().equals(qualification)) {
                     LocalDate existingStart = existingRole.getStartRoleDate();
                     LocalDate existingEnd = existingRole.getEndRoleDate();

                if (!start.isAfter(existingEnd) && !end.isBefore(existingStart)) {
                    throw new IllegalStateException("Overlap Detected! " + employee.getName() + " is already assigned as a " + qualification + " during this timeframe.");
                }
            }
        }

        ProjectRole role = new ProjectRole();
        role.setProject(project);
        role.setEmployee(employee);
        role.setSpecificQualificationUsed(qualification);
        role.setDescription(description);
        role.setStartRoleDate(start);
        role.setEndRoleDate(end);

        project.getProjectRoles().add(role);
        return projectRoleRepository.save(role);
    }
}