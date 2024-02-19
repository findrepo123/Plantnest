package com.project.api.repositories;

import com.project.api.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "orderStatuses", path="order-statuses")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
