package com.project.api.services;


import com.project.api.dtos.ProductFindAllDTO;

import java.util.List;

public interface WishlistService {
    Boolean add(Integer accountId, Integer productId);
    Boolean remove(Integer accountId, Integer productId);
    Boolean isProductExist(Integer accountId, Integer productId);
}
