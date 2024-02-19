package com.project.api.repositories;

import com.project.api.entities.ProductSaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "productSaleTypes", path="product-sale-types")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface ProductSaleTypeRepository extends JpaRepository<ProductSaleType, Integer> {
}
