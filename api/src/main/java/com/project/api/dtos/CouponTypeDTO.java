package com.project.api.dtos;

import com.project.api.entities.Coupon;
import com.project.api.entities.CouponType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CouponTypeDTO {
    private Integer couponTypeId;
    private String typeName;

    public CouponTypeDTO(CouponType couponType) {
        this.couponTypeId = couponType.getCouponTypeId();
        this.typeName = couponType.getTypeName();
    }
}