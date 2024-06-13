package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateProjectsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateProjectsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.repository.EnvironmentsFeatureFlagsRepository;
import com.featureflagmanagment.featureflagmanagment.domain.repository.EnvironmentsRepository;
import com.featureflagmanagment.featureflagmanagment.domain.repository.FeatureFlagsRepository;
import com.featureflagmanagment.featureflagmanagment.domain.repository.ProjectsRepository;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Environments;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.EnvironmentsFeatureFlags;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.FeatureFlags;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Projects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProjectsServiceImpl implements ProjectsService{

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private FeatureFlagsRepository featureFlagsRepository;

    @Autowired
    private EnvironmentsRepository environmentsRepository;

    @Autowired
    private EnvironmentsFeatureFlagsRepository environmentsFeatureFlagsRepository;

    @Override
    public List<Projects> getAllProjects() {
        List<Projects> projects = projectsRepository.findAll();
        return projects.isEmpty() ? null : projects;
    }

    @Override
    public Projects getProjectById(String projectId) {
        return projectsRepository.findById(Long.valueOf(projectId)).orElse(null);
    }

    @Override
    public Boolean removeProjectById(String projectId) {
        Projects project = projectsRepository.findById(Long.valueOf(projectId)).orElse(null);
        if (project == null) {
            return Boolean.FALSE;
        }

        /** Es codigo repetido, pero es mejor validar que se elimine todo feature flag en la tabla pivote
         * para estar seguros. */
        List<Environments> environments = environmentsRepository.findByProjectId(Long.valueOf(projectId));
        if (!environments.isEmpty()) {
            for (Environments environment: environments) {
                List<EnvironmentsFeatureFlags> flags = environmentsFeatureFlagsRepository.findByEnvId(environment.getIdEnvironment());
                if (!flags.isEmpty()) {
                    environmentsFeatureFlagsRepository.deleteAll(flags);
                }
            }
            environmentsRepository.deleteAll(environments);
        }

        List<FeatureFlags> featureFlags = featureFlagsRepository.findByProjectId(Long.valueOf(projectId));
        if (!featureFlags.isEmpty()) {
            for (FeatureFlags featureFlag: featureFlags) {
                List<EnvironmentsFeatureFlags> flags = environmentsFeatureFlagsRepository.findByFlagId(featureFlag.getIdFeatureFlag());
                if (!flags.isEmpty()) {
                    environmentsFeatureFlagsRepository.deleteAll(flags);
                }
            }
            featureFlagsRepository.deleteAll(featureFlags);
        }

        projectsRepository.delete(project);
        return Boolean.TRUE;
    }

    /** Falta manejar la excepcion cuando la primerca condicion se cumple!!! */
    @Override
    public Projects createProject(CreateProjectsRequest request) {
        if (request == null && !StringUtils.hasLength(request.getProjectName().trim())
        && !StringUtils.hasLength(request.getDescription().trim())
        && !request.isEnabled())
            return null; // actualmente es manejado por handleUnknownException

        Projects projects = Projects.builder().projectName(request.getProjectName()).description(request.getDescription()).enabled(request.isEnabled()).createdAt(Date.from(Instant.now())).build();
        return projectsRepository.save(projects);
    }

    @Override
    public Projects updateProject(Long id,UpdateProjectsRequest request) {
        if (request == null && !StringUtils.hasLength(request.getProjectName().trim())
        && !StringUtils.hasLength(request.getDescription().trim())
        && !request.isEnabled())
            return null;

        Projects project = projectsRepository.findById(id).orElse(null);
        if (project == null) {
            return null;
        }

        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setEnabled(request.isEnabled());
        return projectsRepository.save(project);
    }
}
