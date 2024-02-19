package com.project.api.repositories;

import com.project.api.entities.ProductSaleType;
import com.project.api.entities.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "productVariants", path="product-variants")
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
}
