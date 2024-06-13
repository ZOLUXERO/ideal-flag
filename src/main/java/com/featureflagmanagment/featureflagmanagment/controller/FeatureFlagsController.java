package com.featureflagmanagment.featureflagmanagment.controller;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateFeatureFlagsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateFeatureFlagRequest;
import com.featureflagmanagment.featureflagmanagment.domain.service.FeatureFlagsService;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.FeatureFlags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagsController {
    private final FeatureFlagsService featureFlagsService;

    @GetMapping("/feature_flags")
    public ResponseEntity<List<FeatureFlags>> getFeatureFlags() {
        List<FeatureFlags> featureFlags = featureFlagsService.getAllFeatureFlags();
        if (featureFlags != null) {
            return ResponseEntity.ok(featureFlags);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/feature_flags/{featureFlagId}")
    public ResponseEntity<FeatureFlags> getFeatureFlagById(@PathVariable String featureFlagId) {
        log.info("Getting feature flag with id: {}", featureFlagId);
        FeatureFlags featureFlags = featureFlagsService.getFeatureFlagById(featureFlagId);
        if (featureFlags != null) {
            return ResponseEntity.ok(featureFlags);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/feature_flags/{featureFlagId}")
    public ResponseEntity<Void> deleteFeatureFlag(@PathVariable String featureFlagId) {
        log.info("Removing feature flag with id: {}", featureFlagId);
        Boolean removed = featureFlagsService.removeFeatureFlagById(featureFlagId);
        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/feature_flags")
    public ResponseEntity<FeatureFlags> createFeatureFlag(@RequestBody CreateFeatureFlagsRequest request) {
        FeatureFlags createFeatureFlag = featureFlagsService.createFeatureFlag(request);
        if (createFeatureFlag != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createFeatureFlag);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/feature_flags/{featureFlagId}")
    public ResponseEntity<FeatureFlags> updateFeatureFlag(@PathVariable Long featureFlagId, @RequestBody UpdateFeatureFlagRequest request) {
        FeatureFlags updateFeatureFlag = featureFlagsService.updateFeatureFlag(featureFlagId, request);
        if (updateFeatureFlag != null) {
            return ResponseEntity.ok(updateFeatureFlag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
