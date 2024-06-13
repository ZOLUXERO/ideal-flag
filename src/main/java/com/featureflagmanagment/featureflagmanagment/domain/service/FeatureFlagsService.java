package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateFeatureFlagsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateFeatureFlagRequest;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.FeatureFlags;

import java.util.List;

public interface FeatureFlagsService {
    List<FeatureFlags> getAllFeatureFlags();

    FeatureFlags getFeatureFlagById(String featureFlagId);

    Boolean removeFeatureFlagById(String featureFlagId);

    FeatureFlags createFeatureFlag(CreateFeatureFlagsRequest request);

    FeatureFlags updateFeatureFlag(Long id, UpdateFeatureFlagRequest request);
}
