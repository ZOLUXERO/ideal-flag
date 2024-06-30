package com.featureflagmanagment.featureflagmanagment.controller;

import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateProjectsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateProjectsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.service.ProjectsService;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Projects;
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
public class ProjectsController {
    private final ProjectsService projectsService;

    @GetMapping("/projects")
    public ResponseEntity<List<Projects>> getProjects() {
        List<Projects> projects = projectsService.getAllProjects();
        if (projects != null) {
            return ResponseEntity.ok(projects);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<Projects> getProjectById(@PathVariable String projectId) {
        log.info("Get project by id: {}", projectId);
        Projects projects = projectsService.getProjectById(projectId);
        if (projects != null) {
            return ResponseEntity.ok(projects);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable String projectId) {
        Boolean removed = projectsService.removeProjectById(projectId);
        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/projects")
    public ResponseEntity<Projects> createProject(@RequestBody CreateProjectsRequest request) {
        Projects projects = projectsService.createProject(request);
        if (projects != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(projects);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<Projects> updateProject(@PathVariable Long projectId, @RequestBody UpdateProjectsRequest request) {
       Projects project = projectsService.updateProject(projectId, request);
       if (project != null) {
           return ResponseEntity.status(HttpStatus.ACCEPTED).body(project);
       } else {
           return ResponseEntity.notFound().build();
       }
    }
}
