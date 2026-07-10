package com.construction.app.service;

import com.construction.app.domain.Project;
import com.construction.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project saveProject(Project project) {
        validateTargetAudienceAspect(project);
        return projectRepository.save(project);
    }

    private void validateTargetAudienceAspect(Project project) {
        if (project.getProjectCategory() == null) {
            throw new IllegalArgumentException("ProjectCategory (Target Audience) cannot be null.");
        }

        switch (project.getProjectCategory()) {
            case BUSINESS:
                if (project.getNameOfCompany() == null || project.getNameOfCompany().trim().isEmpty()) {
                    throw new IllegalArgumentException("A BUSINESS project must specify the NameOfCompany.");
                }
                project.setResidentCapacity(null);
                break;

            case RESIDENTIAL:
                if (project.getResidentCapacity() == null || project.getResidentCapacity() <= 0) {
                    throw new IllegalArgumentException("A RESIDENTIAL project must specify a valid ResidentCapacity.");
                }
                project.setNameOfCompany(null);
                break;
        }
    }
}