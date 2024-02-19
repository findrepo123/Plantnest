package com.project.api.dtos;

import com.project.api.entities.ProductSize;
import com.project.api.entities.ProductVariant;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductSizeDTO {
    private Integer productSizeId;
    private String sizeName;

    public ProductSizeDTO(ProductSize productSize) {
        this.productSizeId = productSize.getProductSizeId();
        this.sizeName = productSize.getSizeName();
    }
}