package com.project.api.services.impl;

import com.project.api.dtos.CouponDTO;
import com.project.api.entities.Coupon;
import com.project.api.repositories.CouponRepository;
import com.project.api.repositories.OrderRepository;
import com.project.api.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Boolean delete(Integer id) {
        try {
            Coupon coupon = couponRepository.findById(id).get();
            coupon.getOrders().stream().forEach(order -> {
                order.setCoupon(null);
                orderRepository.save(order);
            });
            this.couponRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean delete(List<Coupon> coupons) {
        try {
            coupons.stream().forEach(c -> {
                this.delete(c.getCouponId());
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean isCouponExist(String code) {
        try {
            return couponRepository.existsByCode(code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean isCouponCanBeUsed(String code) {
        try {
            return couponRepository.isCouponCanBeUsed(code, new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CouponDTO findByCode(String code) {
        try {
            Coupon coupon = couponRepository.findCouponByCode(code);
            if(coupon != null) {
                return new CouponDTO(coupon);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
