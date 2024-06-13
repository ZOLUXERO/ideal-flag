package com.featureflagmanagment.featureflagmanagment.controller;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateEnvironmentsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateEnvironmentRequest;
import com.featureflagmanagment.featureflagmanagment.domain.service.EnvironmentsService;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Environments;
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
public class EnvironmentsController {
    private final EnvironmentsService environmentsService;

    @GetMapping("/environments")
    public ResponseEntity<List<Environments>> getEnvironments() {
        List<Environments> environments = environmentsService.getAllEnvironments();
        if (environments != null) {
            return ResponseEntity.ok(environments);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/environments/{environmentId}")
    public ResponseEntity<Environments> getEnvironment(@PathVariable String environmentId) {
        log.info("Getting environment by id: {}", environmentId);
        Environments environment = environmentsService.getEnvironment(environmentId);
        if (environment != null) {
            return ResponseEntity.ok(environment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/environments/{environmentId}")
    public ResponseEntity<Void> deleteEnvironment(@PathVariable String environmentId) {
        Boolean removed = environmentsService.removeEnvironment(environmentId);
        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/environments")
    public ResponseEntity<Environments> createEnvironment(@RequestBody CreateEnvironmentsRequest request) {
        Environments createEnvironment = environmentsService.createEnvironment(request);
        if (createEnvironment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createEnvironment);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/environments/{environmentId}")
    public ResponseEntity<Environments> updateEnvironment(@PathVariable Long environmentId, @RequestBody UpdateEnvironmentRequest request) {
        Environments environment = environmentsService.updateEnvironment(environmentId, request);
        if (environment != null) {
            return ResponseEntity.ok(environment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
