package com.featureflagmanagment.featureflagmanagment.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeatureFlagsEnvironmentsPK implements Serializable {
    private Long idEnvironment;

    @Override
    public int hashCode(){
        return Objects.hash(idEnvironment);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof FeatureFlagsEnvironmentsPK that)) return false;
        return Objects.equals(idEnvironment, that.idEnvironment);
    }
}
