package com.project.api.dtos;

import com.project.api.entities.PaymentMethod;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDTO {
    private Integer paymentMethodId;
    private String methodName;

    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        this.paymentMethodId = paymentMethod.getPaymentMethodId();
        this.methodName = paymentMethod.getMethodName();
    }
}
