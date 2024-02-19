package com.project.api.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "`product_care_guide`")
public class ProductCareGuide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "watering")
    private String watering;

    @Column(name = "light")
    private String light;

    @Column(name = "nutrition")
    private String nutrition;

    @Column(name = "cleaning")
    private String cleaning;

    @Column(name = "pruning")
    private String pruning;

    @Column(name = "bugs")
    private String bugs;

    @Column(name = "trouble")
    private String trouble;

    @Column(name = "warning")
    private String warning;

    public ProductCareGuide() {}

    public ProductCareGuide(int productId, String watering, String light, String nutrition, String cleaning, String pruning, String bugs, String trouble, String warning) {
        this.productId = productId;
        this.watering = watering;
        this.light = light;
        this.nutrition = nutrition;
        this.cleaning = cleaning;
        this.pruning = pruning;
        this.bugs = bugs;
        this.trouble = trouble;
        this.warning = warning;
    }
}
