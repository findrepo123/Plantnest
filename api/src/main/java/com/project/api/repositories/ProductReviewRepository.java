package com.project.api.repositories;

import com.project.api.entities.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "productReviews", path="product-reviews")
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

    List<ProductReview> findByProductProductId(Integer productId);

}
