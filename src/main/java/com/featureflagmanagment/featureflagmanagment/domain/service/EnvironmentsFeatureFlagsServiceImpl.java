package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateEnvironmentsFeatureFlagsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.repository.EnvironmentsFeatureFlagsRepository;
import com.featureflagmanagment.featureflagmanagment.exception.AplicationException;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.EnvironmentsFeatureFlags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentsFeatureFlagsServiceImpl implements EnvironmentsFeatureFlagService {
    @Autowired
    private EnvironmentsFeatureFlagsRepository environmentsFeatureFlagsRepository;

    @Override
    public List<EnvironmentsFeatureFlags> getAllEnvironmentsFeatureFlags() {
        List<EnvironmentsFeatureFlags> environmentsFeatureFlags = environmentsFeatureFlagsRepository.findAll();
        return environmentsFeatureFlags.isEmpty() ? null : environmentsFeatureFlags;
    }

    @Override
    public EnvironmentsFeatureFlags getEnviromentsFeatureFlagById(String environmentsFeatureFlagsId) {
        return environmentsFeatureFlagsRepository.findById(Long.valueOf(environmentsFeatureFlagsId)).orElse(null);
    }

    public List<EnvironmentsFeatureFlags> getAllEnvFf(Long idProject) {
        List<EnvironmentsFeatureFlags> environmentsFeatureFlags = environmentsFeatureFlagsRepository.findFf(idProject);
        return environmentsFeatureFlags.isEmpty() ? null : environmentsFeatureFlags;
    }

    /** Falta manejar la excepcion cuando la primerca condicion se cumple!!! */
    @Override
    public EnvironmentsFeatureFlags update(Long id, UpdateEnvironmentsFeatureFlagsRequest request) {

        EnvironmentsFeatureFlags flag = environmentsFeatureFlagsRepository.findById(id).orElse(null);
        if (flag == null) {
            throw new AplicationException(
                    "Flag Does Not Exist.",
                    String.format("Flag with id=%d not found", id),
                    HttpStatus.NOT_FOUND
            );
        }
        flag.setEnabled(request.isEnabled());

        return environmentsFeatureFlagsRepository.save(flag);
    }
}
