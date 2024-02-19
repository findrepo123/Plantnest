package com.project.api.dtos;

import com.project.api.entities.ProductSaleType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductSaleTypeDTO {
    private Integer productSaleTypeId;
    private String typeName;

    public ProductSaleTypeDTO(ProductSaleType productSaleType) {
        this.productSaleTypeId = productSaleType.getProductSaleTypeId();
        this.typeName = productSaleType.getTypeName();

    }
}