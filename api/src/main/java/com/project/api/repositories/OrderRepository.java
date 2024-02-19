package com.project.api.repositories;

import com.project.api.entities.Order;
import com.project.api.entities.OrderStatus;
import com.project.api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "orders", path="orders")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    Long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(Date start, Date end);
    Long countByCreatedAt(Date today);
    List<Order> findByAccountId(Integer accountId);
}
