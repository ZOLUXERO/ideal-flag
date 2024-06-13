package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateFeatureFlagsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateFeatureFlagRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateProjectsRequest;
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
public class FeatureFlagsServiceImpl implements FeatureFlagsService {
    @Autowired
    private FeatureFlagsRepository featureFlagsRepository;

    @Autowired
    private EnvironmentsRepository environmentsRepository;

    @Autowired
    private EnvironmentsFeatureFlagsRepository environmentsFeatureFlagsRepository;

    @Autowired
    private ProjectsRepository projectsRepository;

    @Override
    public List<FeatureFlags> getAllFeatureFlags() {
        List<FeatureFlags> featureFlags = featureFlagsRepository.findAll();
        return featureFlags.isEmpty() ? null : featureFlags;
    }

    @Override
    public FeatureFlags getFeatureFlagById(String featureFlagId) {
        return featureFlagsRepository.findById(Long.valueOf(featureFlagId)).orElse(null);
    }

    @Override
    public Boolean removeFeatureFlagById(String featureFlagId) {
        FeatureFlags featureFlags = featureFlagsRepository.findById(Long.valueOf(featureFlagId)).orElse(null);
        if (featureFlags == null) {
            return Boolean.FALSE;
        }

        List<EnvironmentsFeatureFlags> flags = environmentsFeatureFlagsRepository.findByFlagId(Long.valueOf(featureFlagId));
        if (flags != null) {
            environmentsFeatureFlagsRepository.deleteAll(flags);
        }

        featureFlagsRepository.delete(featureFlags);
        return Boolean.TRUE;
    }

    /** Falta manejar la excepcion cuando la primerca condicion se cumple!!! */
    @Override
    public FeatureFlags createFeatureFlag(CreateFeatureFlagsRequest request) {
        if (request == null && !StringUtils.hasLength(request.getFlagName().trim())
                && !StringUtils.hasLength(request.getDescription().trim())
                && !request.isEnabled())
            return null; // actualmente es manejado por handleUnknownException

        Optional<Projects> project = projectsRepository.findById(request.getIdProject());
        if (project.isEmpty()) {
            log.info("No project found with id: {}", request.getIdProject());
            throw new AplicationException(
                    "Project not Found.",
                    String.format("Project with id=%d not found", request.getIdProject()),
                    HttpStatus.NOT_FOUND
            );
        }

        List<Environments> environments = environmentsRepository.findByProjectId(request.getIdProject());
        if (environments.isEmpty()) {
            log.info("No environments found");
            throw new AplicationException(
                    "No environments Found in this project, Please Create at least one environment.",
                    String.format("Environments with project id=%d not found", request.getIdProject()),
                    HttpStatus.NOT_FOUND
            );
        }

        FeatureFlags flagExistsWithProjectID = featureFlagsRepository.findByNameAndProjectId(request.getFlagName(), request.getIdProject());
        if (flagExistsWithProjectID != null) {
            throw new AplicationException(
                    "Flag with name " + request.getFlagName() + " already exists in this project, flags should have unique names per project.",
                    String.format("Flag with name=%s already exists in project with id=%d", request.getFlagName(),request.getIdProject()),
                    HttpStatus.BAD_REQUEST
            );
        }

        FeatureFlags flag = FeatureFlags.builder().flagName(request.getFlagName()).description(request.getDescription()).value(request.getValue()).project(project.get()).enabled(request.isEnabled()).createdAt(Date.from(Instant.now())).build();
        FeatureFlags savedFlag = featureFlagsRepository.save(flag);

        /**
         * No se si con esto se esto rompiendo el principio de responsabilidad unica!
         */
        for (Environments environment : environments) {
            EnvironmentsFeatureFlags environmentFlag = new EnvironmentsFeatureFlags();
            environmentFlag.setFeatureFlag(savedFlag);
            environmentFlag.setEnvironments(environment);
            environmentFlag.setCreatedAt(Date.from(Instant.now()));
            environmentsFeatureFlagsRepository.save(environmentFlag);
        }

        return savedFlag;
    }

    @Override
    public FeatureFlags updateFeatureFlag(Long id, UpdateFeatureFlagRequest request) {
       if (request == null && StringUtils.hasLength(request.getDescription()) && request.isEnabled())
           return null;

       FeatureFlags flag = featureFlagsRepository.findById(id).orElse(null);
       if (flag == null) {
           return null;
       }

       flag.setDescription(request.getDescription());
       flag.setEnabled(request.isEnabled());
       return featureFlagsRepository.save(flag);
    }

}
