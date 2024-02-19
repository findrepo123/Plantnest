package com.project.api.dtos;

import com.project.api.entities.District;
import com.project.api.entities.Ward;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DistrictDTO {
    private String code;
    private String fullName;

    public DistrictDTO(District district) {
        this.code = district.getCode();
        this.fullName = district.getFullName();
    }
}
