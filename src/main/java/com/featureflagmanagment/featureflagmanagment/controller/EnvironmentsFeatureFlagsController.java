package com.featureflagmanagment.featureflagmanagment.controller;

import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateEnvironmentsFeatureFlagsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.service.EnvironmentsFeatureFlagService;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.EnvironmentsFeatureFlags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class EnvironmentsFeatureFlagsController {
    private final EnvironmentsFeatureFlagService environmentsFeatureFlagService;

    @GetMapping("/env_ff")
    public ResponseEntity<List<EnvironmentsFeatureFlags>> getEnvironmentsFeatureFlags() {
        List<EnvironmentsFeatureFlags> environmentsFeatureFlags = environmentsFeatureFlagService.getAllEnvironmentsFeatureFlags();
        if (environmentsFeatureFlags != null) {
            return ResponseEntity.ok(environmentsFeatureFlags);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/ff/{id}")
    public ResponseEntity<List<EnvironmentsFeatureFlags>> getFf(@PathVariable Long id) {
        List<EnvironmentsFeatureFlags> environmentsFeatureFlags = environmentsFeatureFlagService.getAllEnvFf(id);
        if (environmentsFeatureFlags != null) {
            return ResponseEntity.ok(environmentsFeatureFlags);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/env_ff/{env_ff_id}")
    public ResponseEntity<EnvironmentsFeatureFlags> getEnvironment(@PathVariable String env_ff_id) {
        log.info("Getting flag and environment by id: {}", env_ff_id);
        EnvironmentsFeatureFlags environmentsFeatureFlags = environmentsFeatureFlagService.getEnviromentsFeatureFlagById(env_ff_id);
        if (environmentsFeatureFlags != null) {
            return ResponseEntity.ok(environmentsFeatureFlags);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ff/env/{id}")
    public ResponseEntity<List<EnvironmentsFeatureFlags>> getFlagsByEnvironment(@PathVariable Long id) {
        log.info("Getting flag by environment id: {}", id);
        List<EnvironmentsFeatureFlags> environmentsFeatureFlags = environmentsFeatureFlagService.getByEnvId(id);
        if (environmentsFeatureFlags != null) {
            return ResponseEntity.ok(environmentsFeatureFlags);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PutMapping("/ff/{id}")
    public ResponseEntity<EnvironmentsFeatureFlags> updateFlag(@PathVariable Long id, @RequestBody UpdateEnvironmentsFeatureFlagsRequest request) {
        EnvironmentsFeatureFlags updateFlag = environmentsFeatureFlagService.update(id, request);
        if (updateFlag != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateFlag);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
