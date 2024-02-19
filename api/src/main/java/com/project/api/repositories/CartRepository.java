package com.project.api.repositories;

import com.project.api.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "carts", path="carts")
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query(value = "SELECT CASE WHEN COUNT(cart_detail_id) > 0 THEN 1 ELSE 0 END FROM CartDetail " +
            "WHERE cart_id=:cartId AND product_id=:productId AND product_variant_id=:productVariantId", nativeQuery = true)
    Integer isCartDetailExist(@Param("cartId") Integer cartId, @Param("productId") Integer productId, @Param("productVariantId") Integer productVariantId);
}
