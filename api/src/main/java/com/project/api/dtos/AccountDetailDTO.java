package com.project.api.dtos;

import com.project.api.entities.Account;
import com.project.api.entities.Image;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountDetailDTO {
    private Integer id;
    private String username;
    private String email;
    private ImageDTO image;
    private RoleDTO role;

    private String phoneNumber;
    private String fullName;
    private Boolean active;
    private Date createdAt;
    private Integer totalOrders;

    private List<AddressDTO> addresses;
    private List<OrderFindAllDTO> orders;
    private List<ProductReviewDTO> productReviews;

    public AccountDetailDTO(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.username = account.getUsername();
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