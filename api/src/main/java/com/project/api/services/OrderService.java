package com.project.api.services;

import com.project.api.dtos.AccountDTO;
import com.project.api.dtos.OrderFindAllDTO;
import com.project.api.dtos.OrderFindDetailDTO;
import com.project.api.dtos.request.OrderRequestDTO;
import com.project.api.entities.Account;
import com.project.api.entities.Order;
import com.project.api.entities.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    List<OrderFindAllDTO> findAll();

    OrderFindDetailDTO findById(Integer orderId);
    Boolean insert(OrderRequestDTO order);
    Boolean updateStatus(Order order, OrderStatus orderStatus);
    Boolean updateStatus(List<Order> orders, OrderStatus orderStatus);
    Long countCompleted();
    Long countProductSold();
    Long countOrdersLastMonth();
    Long countOrdersThisWeek();
    Long countOrdersToday();
    Page<OrderFindAllDTO> findOrders(Pageable pageable, Integer accountId, String orderBy, String searchTerm);

}
