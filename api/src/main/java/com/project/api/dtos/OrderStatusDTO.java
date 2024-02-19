package com.project.api.dtos;

import com.project.api.entities.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusDTO {
    private Integer orderStatusId;
    private String statusName;
    private String description;

    public OrderStatusDTO(OrderStatus orderStatus) {
        this.orderStatusId = orderStatus.getOrderStatusId();
        this.statusName = orderStatus.getStatusName();
        this.description = orderStatus.getDescription();
    }
}
