package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateEnvironmentsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateEnvironmentRequest;
import com.featureflagmanagment.featureflagmanagment.domain.repository.EnvironmentsFeatureFlagsRepository;
import com.featureflagmanagment.featureflagmanagment.domain.repository.EnvironmentsRepository;
import com.featureflagmanagment.featureflagmanagment.domain.repository.FeatureFlagsRepository;
import com.featureflagmanagment.featureflagmanagment.domain.repository.ProjectsRepository;
import com.featureflagmanagment.featureflagmanagment.exception.AplicationException;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Environments;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.EnvironmentsFeatureFlags;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.FeatureFlags;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Projects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EnvironmentsServiceImpl implements EnvironmentsService {

    /** Index empieza en 0, esto es 10 en realidad. */
    static int MAX_ENVS_PER_PROJECT = 9;

    @Autowired
    private EnvironmentsRepository environmentsRepository;

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private FeatureFlagsRepository featureFlagsRepository;

    @Autowired
    private EnvironmentsFeatureFlagsRepository environmentsFeatureFlagsRepository;

    @Override
    public List<Environments> getAllEnvironments() {
        List<Environments> environments = environmentsRepository.findAll();
        return environments.isEmpty() ? null : environments;
    }

    @Override
    public Environments getEnvironment(String environmentId) {
        return environmentsRepository.findById(Long.valueOf(environmentId)).orElse(null);
    }

    @Override
    public Boolean removeEnvironment(String environmentId) {
        Environments environment = environmentsRepository.findById(Long.valueOf(environmentId)).orElse(null);
        if (environment == null) {
            return Boolean.FALSE;
        }

        List<EnvironmentsFeatureFlags> flags = environmentsFeatureFlagsRepository.findByEnvId(Long.valueOf(environmentId));
        if (flags != null) {
            environmentsFeatureFlagsRepository.deleteAll(flags);
        }
        environmentsRepository.delete(environment);
        return Boolean.TRUE;
    }

    /** Falta manejar la excepcion cuando la primerca condicion se cumple!!! */
    @Override
    public Environments createEnvironment(CreateEnvironmentsRequest request) {
        if (request == null && !StringUtils.hasLength(request.getName().trim())
                && !StringUtils.hasLength(request.getDescription().trim())
                && !request.isEnabled())
            return null; // actualmente es manejado por handleUnknownException

        Optional<Projects> project = projectsRepository.findById(request.getIdProject());
        if (project.isEmpty()) {
            throw new AplicationException(
                    "Project not Found.",
                    String.format("Project with id=%d not found", request.getIdProject()),
                    HttpStatus.NOT_FOUND
            );
        }

        Environments envExistsInProject = environmentsRepository.findByNameAndProjectId(request.getName(), request.getIdProject());
        List<Environments> envsWithProjectId = environmentsRepository.findByProjectId(request.getIdProject());
        if (envExistsInProject != null || envsWithProjectId.size() > MAX_ENVS_PER_PROJECT) {
            throw new AplicationException(
                    "Problem Creating Environment.",
                    String.format("You have reached the maximum allowed environments per project or an environment whit this name already exists, max allowed are %d", MAX_ENVS_PER_PROJECT + 1),
                    HttpStatus.BAD_REQUEST
            );
        }

        Environments environment = Environments.builder().name(request.getName()).description(request.getDescription()).project(project.get()).enabled(request.isEnabled()).createdAt(Date.from(Instant.now())).build();
        Environments savedEnvironment = environmentsRepository.save(environment);

        List<FeatureFlags> flags = featureFlagsRepository.findByProjectId(request.getIdProject());
        if (!flags.isEmpty()) {
            for (FeatureFlags flag : flags) {
                EnvironmentsFeatureFlags environmentFlag = new EnvironmentsFeatureFlags();
                environmentFlag.setFeatureFlag(flag);
                environmentFlag.setEnvironments(savedEnvironment);
                environmentsFeatureFlagsRepository.save(environmentFlag);
            }
        }

        return savedEnvironment;
    }

    @Override
    public Environments updateEnvironment(Long id, UpdateEnvironmentRequest request) {
        if (request == null && !StringUtils.hasLength(request.getDescription()) && request.isEnabled())
            return null;

        Environments environment = environmentsRepository.findById(id).orElse(null);
        if (environment == null) {
            return null;
        }

        environment.setDescription(request.getDescription());
        environment.setEnabled(request.isEnabled());
        return environmentsRepository.save(environment);
    }

}
