package com.featureflagmanagment.featureflagmanagment.domain.service;


import com.featureflagmanagment.featureflagmanagment.domain.dto.CreateProjectsRequest;
import com.featureflagmanagment.featureflagmanagment.domain.dto.UpdateProjectsRequest;
import com.featureflagmanagment.featureflagmanagment.persistence.entity.Projects;

import java.util.List;

public interface ProjectsService {
    List<Projects> getAllProjects();

    Projects getProjectById(String projectId);

    Boolean removeProjectById(String projectId);

    Projects createProject(CreateProjectsRequest request);

    Projects updateProject(Long id, UpdateProjectsRequest request);
}
