package com.project.api.services.impl;

import com.project.api.dtos.ProductReviewDTO;
import com.project.api.entities.ProductReview;
import com.project.api.repositories.ProductReviewRepository;
import com.project.api.services.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    @Autowired
    private ProductReviewRepository productReviewRepository;


    @Override
    public List<ProductReviewDTO> findByProductId(Integer productId) {
        try {
            return productReviewRepository.findByProductProductId(productId).stream()
                    .map(ProductReviewDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
