package com.project.api.controllers.customer;

import com.project.api.dtos.CouponDTO;
import com.project.api.entities.Coupon;
import com.project.api.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/coupons")
public class Coupon2Controller {

    @Autowired
    private CouponService couponService;

    @GetMapping("isCouponExist")
    public ResponseEntity<Boolean> isCouponExist(@RequestParam String code) {
        try {
            return new ResponseEntity<>(this.couponService.isCouponExist(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("isCouponCanBeUsed")
    public ResponseEntity<Boolean> isCouponCanBeUsed(@RequestParam String code) {
        try {
            return new ResponseEntity<>(this.couponService.isCouponCanBeUsed(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findByCode")
    public ResponseEntity<CouponDTO> findByCode(@RequestParam String code) {
        try {
            return new ResponseEntity<>(this.couponService.findByCode(code), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
