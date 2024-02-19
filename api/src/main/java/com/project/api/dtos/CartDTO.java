package com.project.api.dtos;

import com.project.api.entities.Account;
import com.project.api.entities.Cart;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CartDTO {
    private Integer cartId;
    private Date createdAt;
    private Date updatedAt;
    private List<CartDetailDTO> cartDetails;

    public CartDTO(Cart cart) {
        this.cartId = cart.getCartId();
        this.createdAt = cart.getCreatedAt();
        this.updatedAt = cart.getUpdatedAt();
        this.cartDetails = cart.getCartDetails().stream().map(CartDetailDTO::new).collect(Collectors.toList());
    }
}
