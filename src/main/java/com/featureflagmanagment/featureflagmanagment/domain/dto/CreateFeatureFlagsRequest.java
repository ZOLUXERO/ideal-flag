package com.featureflagmanagment.featureflagmanagment.domain.dto;

import com.featureflagmanagment.featureflagmanagment.persistence.entity.Projects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeatureFlagsRequest {
    private String flagName;
    private String description;
    private String value;
    private Long idProject;
    private boolean enabled;
}
