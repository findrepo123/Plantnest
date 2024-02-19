package com.project.api.dtos.request;

import com.project.api.dtos.AddressDTO;
import com.project.api.dtos.CouponDTO;
import com.project.api.dtos.OrderStatusDTO;
import com.project.api.dtos.PaymentMethodDTO;
import com.project.api.entities.OrderStatus;
import com.project.api.entities.PaymentMethod;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDTO {
    private String accountEmail;
    private AddressDTO address;
    private CouponDTO coupon;
    private OrderStatusDTO orderStatus;
    private PaymentMethodDTO paymentMethod;
    private List<ProductRequestDTO> products;
    private BigDecimal totalPrice;
    private Integer totalQuantity;

    public OrderRequestDTO(String accountEmail, AddressDTO address, CouponDTO coupon,
                           OrderStatus orderStatus, PaymentMethod paymentMethod,
                           List<ProductRequestDTO> products, BigDecimal totalPrice, Integer totalQuantity) {
        this.accountEmail = accountEmail;
        this.address = address;
        this.coupon = coupon;
        this.orderStatus = new OrderStatusDTO(orderStatus);
        this.paymentMethod = new PaymentMethodDTO(paymentMethod);
        this.products = products;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }
}
