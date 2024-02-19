package com.project.api.dtos;

import com.project.api.entities.ProductSale;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductSaleDTO {
    private Integer productSaleId;
    private ProductSaleTypeDTO productSaleType;
    private String saleName;
    private int discount;
    private String description;
    private boolean active;
    private Date startedAt;
    private Date expiredAt;

    public ProductSaleDTO(ProductSale productSale) {
        this.productSaleId = productSale.getProductSaleId();
        this.productSaleType = new ProductSaleTypeDTO(productSale.getProductSaleType());
        this.saleName = productSale.getSaleName();
        this.discount = productSale.getDiscount();
        this.description = productSale.getDescription();
        this.active = productSale.isActive();
        this.startedAt = productSale.getStartedAt();
        this.expiredAt = productSale.getExpiredAt();

    }
}