package com.project.api.dtos;

import com.project.api.entities.Account;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private String fullName;
    private Boolean active;
    private Date createdAt;
    private Integer totalOrders;
    private ImageDTO image;
    private RoleDTO role;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.phoneNumber = account.getPhoneNumber();
        this.fullName = account.getFullName();
        this.active = account.getActive();
        this.createdAt = account.getCreatedAt();
        this.totalOrders = account.getOrders().size();
        if(account.getImage() != null) {
            this.image = new ImageDTO(account.getImage());
        }

        if(account.getRole() != null) {
            this.role = new RoleDTO(account.getRole());
        }
    }

}