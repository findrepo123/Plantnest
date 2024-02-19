package com.project.api.services;

import com.project.api.dtos.CartDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    CartDTO findAll(Integer accountId);
    Boolean save(Integer accountId, Integer productId, Integer productVariantId, Integer quantity);
    Boolean remove(Integer accountId, Integer productId, Integer productVariantId);
    Boolean isExists(Integer cartId, Integer productId, Integer productVariantId);
    Boolean removeAll(Integer accountId);
    Boolean canAddToCart(Integer accountId, Integer productId, Integer productVariantId, Integer quantity);

}
