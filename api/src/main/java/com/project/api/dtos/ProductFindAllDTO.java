package com.project.api.dtos;

import com.project.api.entities.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ProductFindAllDTO {
    private Integer productId;
    private String productName;
    private String slug;

    private CatalogDTO catalog;
    private ProductSaleDTO productSale;
    private PlantingDifficultyLevelDTO plantingDifficultyLevel;

    private String description;
    private String imageUrl;
    private Boolean active;
    private Boolean sale;
    private Boolean top;
    private Boolean new_;
    private Date createdAt;
    private Date updatedAt;
    private Integer totalSold;
    private Integer totalLikes;
    private Integer totalRating;
    private Double avgRating;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<ProductSizeDTO> sizes;

    public ProductFindAllDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.slug = product.getSlug();
        this.description = product.getDescription();
        if(product.getCatalog() != null ) {
            this.catalog = new CatalogDTO(product.getCatalog(), false);
        }
        if(product.getProductSale() != null) {
            this.productSale = new ProductSaleDTO(product.getProductSale());
        }
        if(product.getPlantingDifficultyLevel() != null) {
            this.plantingDifficultyLevel = new PlantingDifficultyLevelDTO(product.getPlantingDifficultyLevel());
        }
        this.sizes = product.getProductVariants().stream()
                .map(variant -> variant.getProductSize())
                .map(ProductSizeDTO::new)
                .collect(Collectors.toList());
        this.imageUrl = product.getImages().get(0).getImageUrl();
        this.active = product.getActive();
        this.sale = product.getSale();
        this.top = product.getTop();
        this.new_ = product.getNew_();
        List<BigDecimal> minAndMaxPrice = findMinAndMaxPrice(product);
        this.minPrice = minAndMaxPrice.get(0);
        this.maxPrice = minAndMaxPrice.get(1);
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    private List<BigDecimal> findMinAndMaxPrice(Product product) {
        BigDecimal minPrice = BigDecimal.valueOf(10000);
        BigDecimal maxPrice = BigDecimal.valueOf(0);
        Set<ProductVariant> variants = product.getProductVariants();

        if(variants.size() == 1) {
            minPrice = variants.iterator().next().getPrice();
            maxPrice = variants.iterator().next().getPrice();
        } else if (variants.size() > 1) {
            for (ProductVariant variant : variants) {
                int compareValue = variant.getPrice().compareTo(minPrice);
                if(compareValue == - 1) {
                    minPrice = variant.getPrice();
                } else if (compareValue == 1) {
                    maxPrice = variant.getPrice();
                }
            }
        }
        List<BigDecimal> minAndMax = new ArrayList<>();
        minAndMax.add(minPrice);
        minAndMax.add(maxPrice);
        return  minAndMax;
    }

}
