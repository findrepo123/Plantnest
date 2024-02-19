package com.project.api.services;


import com.project.api.dtos.*;
import com.project.api.entities.Product;
import com.project.api.entities.ProductSale;
import com.project.api.entities.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDetailDTO findById(Integer productId);
    ProductDetailDTO findBySlug(String slug);
    List<ProductFindAllDTO> findAllDTO();
    List<ProductFindAllDTO> findTop10SaleProducts();
    List<ProductFindAllDTO> findTop10Products();
    Product save(Product product);
    Boolean update(Product product);
    Boolean updateNewStatus(List<Product> products, boolean new_);
    Boolean updateTopStatus(List<Product> products, boolean top);
    Boolean updateActiveStatus(List<Product> products, boolean active);
    Boolean updateProductSale(List<Product> products, ProductSale productSale);
    Boolean updateStatuses(List<Product> products, boolean new_, boolean top, boolean active, ProductSale productSale);
    Boolean delete(Integer productId);
    Boolean delete(List<Product> products);
    Boolean existById(Integer productId);
    List<ProductFindAllDTO> findByNameKeyword(String keyword);

    List<ProductVariantDTO> findVariantsByProductId(Integer productId);
    List<ProductSizeDTO> findSizesByProductId(Integer productId);
    BigDecimal findPrice(Integer productId, ProductSize productSize);
    Integer findMaxQuantity(Integer productId, ProductSize productSize, BigDecimal price);
    List<ProductReviewDTO> findProductReviews(Integer productId);
    Integer countTotalComments(Integer productId);
    Long count();
    List<ProductSizeDTO> findAllSizes();
    Page<ProductFindAllDTO> findByPages(Pageable pageable, String catalog, String size, String level, String orderBy, String searchTerm);
    Boolean comment(Integer productId, String username, Integer ratingStar, String content);
}
