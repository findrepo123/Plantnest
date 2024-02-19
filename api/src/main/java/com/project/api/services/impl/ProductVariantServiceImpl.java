package com.project.api.services.impl;

import com.project.api.entities.ProductVariant;
import com.project.api.repositories.ProductSizeRepository;
import com.project.api.repositories.ProductVariantRepository;
import com.project.api.services.ProductVariantService;
import com.project.api.utilities.ImageUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductSizeRepository sizeRepository;

    @Autowired
    private ImageUploadUtils imageUploadUtils;

    @Override
    public ProductVariant findById(Integer id) {
        return productVariantRepository.findById(id).get();
    }

    @Override
    public Boolean deleteById(Integer id) {
        try {
            ProductVariant variant = this.productVariantRepository.findById(id).get();
            productVariantRepository.deleteById(variant.getProductVariantId());

            if (variant.getImage() != null) {
                imageUploadUtils.delete("variant", variant.getImage().getImageUrl());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
