package com.featureflagmanagment.featureflagmanagment.domain.service;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateEnvironmentsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateEnvironmentRequest;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Environments;

import java.util.List;

public interface EnvironmentsService {
    List<Environments> getAllEnvironments();

    Environments getEnvironment(String environmentId);

    Boolean removeEnvironment(String environmentId);

    Environments createEnvironment(CreateEnvironmentsRequest request);

    Environments updateEnvironment(Long environmentId, UpdateEnvironmentRequest request);
}
