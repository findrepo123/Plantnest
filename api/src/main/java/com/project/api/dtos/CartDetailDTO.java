package com.project.api.dtos;

import com.project.api.entities.CartDetail;
import com.project.api.entities.Product;
import com.project.api.entities.ProductVariant;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartDetailDTO {
    private Integer cartDetailId;
    private ProductFindAllDTO product;
    private ProductVariantDTO productVariant;
    private int quantity;

    public CartDetailDTO(CartDetail cartDetail) {
        this.cartDetailId = cartDetail.getCartDetailId();
        this.product = new ProductFindAllDTO(cartDetail.getProduct());
        this.productVariant = new ProductVariantDTO(cartDetail.getProductVariant());
        this.quantity = cartDetail.getQuantity();
    }
}
