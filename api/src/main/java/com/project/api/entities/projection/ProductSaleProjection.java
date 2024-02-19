package com.project.api.entities.projection;

import com.project.api.entities.Coupon;
import com.project.api.entities.CouponType;
import com.project.api.entities.ProductSale;
import com.project.api.entities.ProductSaleType;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "sale-full", types = { ProductSale.class })
public interface ProductSaleProjection {
    Integer getProductSaleId();
    String getSaleName();
    String getDescription();
    Integer getDiscount();

    Boolean getActive();
    Date getStartedAt();
    Date getExpiredAt();
    ProductSaleType getProductSaleType();
}
