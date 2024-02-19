package com.project.api.dtos;

import com.project.api.entities.ProductCareGuide;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class ProductCareGuideDTO {
    private int productId;
    private String watering;
    private String light;
    private String nutrition;
    private String cleaning;
    private String pruning;
    private String bugs;
    private String trouble;
    private String warning;

    public ProductCareGuideDTO(ProductCareGuide guide) {
        this.productId = guide.getProductId();
        this.watering = guide.getWatering();
        this.light = guide.getLight();
        this.nutrition = guide.getNutrition();
        this.cleaning = guide.getCleaning();
        this.pruning = guide.getPruning();
        this.bugs = guide.getBugs();
        this.trouble = guide.getTrouble();
        this.warning = guide.getWatering();

    }
}
