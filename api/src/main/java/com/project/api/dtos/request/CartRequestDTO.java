package com.project.api.dtos.request;

import lombok.*;

import lombok.Data;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {
    private Integer accountId;
    private Integer productId;
    private Integer productVariantId;
    private Integer quantity;
}
