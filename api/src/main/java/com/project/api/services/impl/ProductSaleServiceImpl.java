package com.project.api.services.impl;

import com.project.api.entities.Coupon;
import com.project.api.entities.ProductSale;
import com.project.api.repositories.CouponRepository;
import com.project.api.repositories.OrderRepository;
import com.project.api.repositories.ProductRepository;
import com.project.api.repositories.ProductSaleRepository;
import com.project.api.services.CouponService;
import com.project.api.services.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSaleServiceImpl implements ProductSaleService {

    @Autowired
    private ProductSaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Boolean updateStatus(List<ProductSale> sales, boolean active) {
        try {
            sales.stream().forEach(sale -> {
                ProductSale oldSale = saleRepository.findById(sale.getProductSaleId()).get();
                oldSale.setActive(active);
                saleRepository.save(oldSale);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean delete(Integer id) {
        try {
            ProductSale sale = saleRepository.findById(id).get();
            sale.getProducts().stream().forEach(product -> {
                product.setProductSale(null);
                productRepository.save(product);
            });
            this.saleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean delete(List<ProductSale> sales) {
        try {
            sales.stream().forEach(s -> {
                this.delete(s.getProductSaleId());
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
