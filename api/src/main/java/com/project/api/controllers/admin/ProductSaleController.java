package com.project.api.controllers.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.entities.Coupon;
import com.project.api.entities.ProductSale;
import com.project.api.services.CouponService;
import com.project.api.services.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/product-sales")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class ProductSaleController {

    @Autowired
    private ProductSaleService saleService;

    @DeleteMapping("delete/{saleId}")
    public ResponseEntity delete(@PathVariable("saleId") Integer saleId) {
        try {
            this.saleService.delete(saleId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("delete-sales")
    public ResponseEntity delete(@RequestBody List<ProductSale> sales) {
        try {
            this.saleService.delete(sales);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-status-sales")
    public ResponseEntity<Boolean> updateStatus(
            @RequestParam("sales") String salesJson,
            @RequestParam("active") Boolean active) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ProductSale> sales = mapper.readValue(salesJson, new TypeReference<List<ProductSale>>() {});

            this.saleService.updateStatus(sales, active);

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
