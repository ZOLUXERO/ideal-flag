package com.featureflagmanagment.featureflagmanagment.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="feature_flag")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FeatureFlags {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long idFeatureFlag;

    //@Column(name = "id_project", nullable = false)
    //private long idProject;

    @NotEmpty
    @Size(min = 3, max = 32, message = "Name must be between 3 and 32 characters long")
    @Column(name = "name", unique = false)
    private String flagName;

    @Column(name = "description")
    private String description;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "id_project", updatable = false)
    private Projects project;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
