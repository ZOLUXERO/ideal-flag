package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateEnvironmentsFeatureFlagsRequest;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.EnvironmentsFeatureFlags;

import java.util.List;

public interface EnvironmentsFeatureFlagService {
    List<EnvironmentsFeatureFlags> getAllEnvironmentsFeatureFlags();

    List<EnvironmentsFeatureFlags> getAllEnvFf(Long idProject);

    EnvironmentsFeatureFlags getEnviromentsFeatureFlagById(String environmentFeatureFlagId);

    List<EnvironmentsFeatureFlags> getByEnvId(Long idEnvironment);

    EnvironmentsFeatureFlags update(Long id,  UpdateEnvironmentsFeatureFlagsRequest request);
}
