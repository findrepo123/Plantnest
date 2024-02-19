package com.project.api.repositories;

import com.project.api.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "cartDetails", path="cart-details")
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
}
