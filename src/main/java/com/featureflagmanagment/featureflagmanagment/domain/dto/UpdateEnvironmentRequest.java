package com.featureflagmanagment.featureflagmanagment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEnvironmentRequest {
    private String description;
    private boolean enabled;
}
