package com.project.api.controllers.admin;

import com.project.api.dtos.CouponDTO;
import com.project.api.entities.Coupon;
import com.project.api.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/coupons")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @DeleteMapping("delete/{couponId}")
    public ResponseEntity delete(@PathVariable("couponId") Integer couponId) {
        try {
            this.couponService.delete(couponId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("delete-coupons")
    public ResponseEntity<Boolean> delete(@RequestBody List<Coupon> coupons) {
        try {
            this.couponService.delete(coupons);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

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
