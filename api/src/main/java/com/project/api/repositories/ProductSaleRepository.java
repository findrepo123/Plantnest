package com.project.api.repositories;

import com.project.api.entities.ProductReview;
import com.project.api.entities.ProductSale;
import com.project.api.entities.projection.ProductSaleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "productSales", path="product-sales",
        excerptProjection = ProductSaleProjection.class)
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface ProductSaleRepository extends JpaRepository<ProductSale, Integer> {
}
