package com.project.api.dtos;

import com.project.api.entities.Ward;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WardDTO {
    private String code;
    private String fullName;

    public WardDTO(Ward ward) {
        this.code = ward.getCode();
        this.fullName = ward.getFullName();
    }
}
