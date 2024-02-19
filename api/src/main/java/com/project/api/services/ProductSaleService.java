package com.project.api.services;


import com.project.api.entities.Coupon;
import com.project.api.entities.ProductSale;

import java.util.List;

public interface ProductSaleService {

    Boolean updateStatus(List<ProductSale> sales, boolean active);
    Boolean delete(Integer id);
    Boolean delete(List<ProductSale> sales);
}
