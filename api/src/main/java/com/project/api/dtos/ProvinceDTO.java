package com.project.api.dtos;

import com.project.api.entities.District;
import com.project.api.entities.Province;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProvinceDTO {
    private String code;
    private String fullName;

    public ProvinceDTO(Province province) {
        this.code = province.getCode();
        this.fullName = province.getFullName();
    }
}
