package com.project.api.dtos;

import com.project.api.entities.Image;
import com.project.api.entities.PlantingDifficultyLevel;
import com.project.api.entities.Product;
import jdk.jfr.Category;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailDTO {
    private Integer productId;
    private String productName;
    private String slug;

    private CatalogDTO catalog;
    private ProductSaleDTO productSale;
    private PlantingDifficultyLevelDTO plantingDifficultyLevel;
    private ProductCareGuideDTO productCareGuide;
    private List<ProductVariantDTO> productVariants;

    private String description;
    private List<String> imageUrls;
    private String imageSizeGuideUrl;
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

    public ProductDetailDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.slug = product.getSlug();
        if(product.getCatalog() != null ) {
            this.catalog = new CatalogDTO(product.getCatalog(), false);
        }
        if(product.getProductSale() != null) {
            this.productSale = new ProductSaleDTO(product.getProductSale());
        }
        if(product.getPlantingDifficultyLevel() != null) {
            this.plantingDifficultyLevel = new PlantingDifficultyLevelDTO(product.getPlantingDifficultyLevel());
        }
        if(product.getProductCareGuide() != null) {
            this.productCareGuide = new ProductCareGuideDTO(product.getProductCareGuide());
        }

        this.imageUrls = product.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        this.productVariants = product.getProductVariants().stream()
                .map(ProductVariantDTO::new)
                .collect(Collectors.toList());
        this.imageSizeGuideUrl = product.getImageSizeGuide().getImageUrl();
        this.description = product.getDescription();
        this.active = product.getActive();
        this.sale = product.getSale();
        this.top = product.getTop();
        this.new_ = product.getNew_();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();

    }
}
