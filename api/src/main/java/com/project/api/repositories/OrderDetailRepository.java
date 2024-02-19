package com.project.api.repositories;

import com.project.api.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "orderDetails", path="order-details")
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
}
