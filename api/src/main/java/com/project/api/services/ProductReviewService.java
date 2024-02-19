package com.project.api.services;

import com.project.api.dtos.ProductReviewDTO;
import com.project.api.entities.ProductReview;

import java.util.List;

public interface ProductReviewService {
    List<ProductReviewDTO> findByProductId(Integer productId);
}
