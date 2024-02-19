package com.project.api.dtos;

import com.project.api.entities.ProductReview;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductReviewDTO {
    private Integer productReviewId;
    private String imageUrl;
    private String fullName;
    private String content;
    private Integer rating;
    private Date createdAt;

    public ProductReviewDTO(ProductReview productReview) {
        this.productReviewId = productReview.getProductReviewId();
        this.imageUrl = productReview.getAccount().getImage().getImageUrl();
        this.fullName = productReview.getAccount().getFullName();
        this.content = productReview.getContent();
        this.rating = productReview.getRating();
        this.createdAt = productReview.getCreatedAt();
    }
}
