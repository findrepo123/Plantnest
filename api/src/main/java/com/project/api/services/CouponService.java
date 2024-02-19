package com.project.api.services;


import com.project.api.dtos.CouponDTO;
import com.project.api.entities.Coupon;

import java.util.List;

public interface CouponService {
    Boolean delete(Integer id);
    Boolean delete(List<Coupon> coupons);
    Boolean isCouponExist(String code);
    Boolean isCouponCanBeUsed(String code);
    CouponDTO findByCode(String code);
}
