package com.project.api.dtos;

import com.project.api.entities.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDTO  implements java.io.Serializable {
    private Integer roleId;
    private String name;

    public RoleDTO(Role role) {
        this.roleId = role.getRoleId();
        this.name = role.getName();
    }

}
