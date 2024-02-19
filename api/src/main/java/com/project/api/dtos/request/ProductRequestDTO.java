package com.project.api.dtos.request;

import com.project.api.dtos.ProductSizeDTO;
import com.project.api.dtos.ProductVariantDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequestDTO {
    private Integer productId;
    private String productName;
    private ProductVariantDTO productVariant;
    private BigDecimal price;
    private Integer quantity;

    public ProductRequestDTO() {}

    public ProductRequestDTO(Integer productId, String productName, ProductVariantDTO productVariant, BigDecimal price, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productVariant = productVariant;
        this.price = price;
        this.quantity = quantity;
    }
}
