package com.project.api.dtos;

import com.project.api.entities.OrderDetail;
import com.project.api.entities.Product;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailDTO {
    private Integer orderDetailId;
    private Integer productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private String size;
    private BigDecimal price;

    public OrderDetailDTO(OrderDetail orderDetail){
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.productName = orderDetail.getProductName();
        this.quantity = orderDetail.getQuantity();
        this.size = orderDetail.getSize();
        this.price = orderDetail.getPrice();
        if(orderDetail.getProduct() != null) {
            this.productId = orderDetail.getProduct().getProductId();
            this.imageUrl = orderDetail.getProduct().getImages().iterator().next().getImageUrl();
        }
    }

}
