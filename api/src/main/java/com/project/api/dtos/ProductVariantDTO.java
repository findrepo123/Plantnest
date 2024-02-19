package com.project.api.dtos;

import com.project.api.entities.ProductVariant;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductVariantDTO {
    private Integer productVariantId;
    private int height;
    private int width;
    private int quantity;
    private BigDecimal price;
    private ProductSizeDTO productSize;
    private String imageUrl;

    public ProductVariantDTO(ProductVariant variant) {
        this.productVariantId = variant.getProductVariantId();
        this.height = variant.getHeight();
        this.width = variant.getWidth();
        this.quantity = variant.getQuantity();
        this.price = variant.getPrice();
        this.productSize = new ProductSizeDTO(variant.getProductSize());
        if(variant.getImage() != null) {
            this.imageUrl = variant.getImage().getImageUrl();
        }
    }
}