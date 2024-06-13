package com.featureflagmanagment.featureflagmanagment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEnvironmentsRequest {
    private String name;
    private String description;
    private Long idProject;
    private boolean enabled;
}
