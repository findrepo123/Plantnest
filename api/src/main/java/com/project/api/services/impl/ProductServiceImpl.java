package com.project.api.services.impl;

import com.project.api.dtos.*;
import com.project.api.entities.*;
import com.project.api.repositories.*;
import com.project.api.services.OrderDetailService;
import com.project.api.services.ProductReviewService;
import com.project.api.services.ProductService;
import com.project.api.services.ProductVariantService;
import com.project.api.specifications.ProductSpecification;
import com.project.api.utilities.ImageUploadUtils;
import com.project.api.utilities.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private ImageUploadUtils imageUploadUtils;

    @Autowired
    private ProductVariantService productVariantService;

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Override
    public ProductDetailDTO findById(Integer productId) {
        try {
            Optional<Product> product =  productRepository.findById(productId);
            return product.isPresent() ? mapToFindDetail(product.get()) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductDetailDTO findBySlug(String slug) {
        try {
            Optional<Product> product =  productRepository.findBySlug(slug);

            return product.isPresent() ? mapToFindDetail(product.get()) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ProductDetailDTO mapToFindDetail(Product product) {
        ProductDetailDTO productDTO = new ProductDetailDTO(product);
        productDTO.setTotalLikes(productRepository.countTotalLikes(product.getProductId()));
        productDTO.setTotalSold(productRepository.countTotalSold(product.getProductId()));
        productDTO.setTotalRating(productRepository.countTotalRating(product.getProductId()));
        productDTO.setAvgRating(productRepository.countAvgRating(product.getProductId()));
        List<BigDecimal> minAndMaxPrice = findMinAndMaxPrice(product.getProductId());
        productDTO.setMinPrice(minAndMaxPrice.get(0));
        productDTO.setMaxPrice(minAndMaxPrice.get(1));
        return productDTO;
    }



    @Override
    public List<ProductFindAllDTO> findAllDTO() {
        List<ProductFindAllDTO> products = productRepository.findAll()
                .stream()
                .map(ProductFindAllDTO::new)
                .collect(Collectors.toList());
        return mapToFindAll(products);
    }

    private List<ProductFindAllDTO> mapToFindAll(List<ProductFindAllDTO> products) {
        products.forEach(pro -> {
            pro.setImageUrl(productRepository.getImageUrls(pro.getProductId()).get(0));
            pro.setTotalLikes(productRepository.countTotalLikes(pro.getProductId()));
            pro.setTotalSold(productRepository.countTotalSold(pro.getProductId()));
            pro.setTotalRating(productRepository.countTotalRating(pro.getProductId()));
            pro.setAvgRating(productRepository.countAvgRating(pro.getProductId()));
        });
        return products;
    }

    @Override
    public List<ProductFindAllDTO> findTop10SaleProducts() {
        List<ProductFindAllDTO> products = productRepository.findTop10BySaleOrderByCreatedAtDesc(true)
                .stream()
                .map(ProductFindAllDTO::new)
                .collect(Collectors.toList());
        return mapToFindAll(products);
    }

    @Override
    public List<ProductFindAllDTO> findTop10Products() {
        List<ProductFindAllDTO> products = productRepository.findTop10ByTopOrderByCreatedAtDesc(true)
                .stream()
                .map(ProductFindAllDTO::new)
                .collect(Collectors.toList());
        return mapToFindAll(products);
    }

    @Override
    public Product save(Product product) {
        try {
            product.getImages().forEach(image -> {
                String imgFileName = imageUploadUtils.uploadImgBase64("product", image);
                image.setImageUrl(imgFileName);
            });
            if(product.getImageSizeGuide() != null) {
                Image sizeGuideImg = new Image(imageUploadUtils.uploadImgBase64("size_guide", product.getImageSizeGuide()));
                product.setImageSizeGuide(sizeGuideImg);
            }

            product.getProductVariants().forEach(variant -> {
                if(variant.getImage() != null) {
                    String variantImgFileName = imageUploadUtils.uploadImgBase64("variant", variant.getImage());
                    variant.getImage().setImageUrl(variantImgFileName);
                }
                if(variant.getProductSize().getProductSizeId() == null) {
                    ProductSize insertedSize = this.productSizeRepository.save(variant.getProductSize());
                    variant.setProductSize(insertedSize);
                }
                variant.setProduct(product);
            });
            product.setCreatedAt(new Date());
            product.setUpdatedAt(new Date());
            product.setSlug(StringUtils.convertToSlug(product.getProductName()));
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean update(Product product) {
        try {
            Product oldProduct = productRepository.findById(product.getProductId()).get();

            Image imgSizeGuide = product.getImageSizeGuide();
            if(imgSizeGuide != null) {
                if(!imgSizeGuide.getImageUrl().startsWith("http")) {
                    imageUploadUtils.delete("size_guide", oldProduct.getImageSizeGuide().getImageUrl());

                    String imgFileName = imageUploadUtils.uploadImgBase64("size_guide", imgSizeGuide);
                    imgSizeGuide.setImageUrl(imgFileName);
                } else {
                    if(oldProduct.getImageSizeGuide() != null) {
                        product.setImageSizeGuide(oldProduct.getImageSizeGuide());
                    }
                }
            }

//          when upload new images
            if(product.getImages().size() > 0 && !product.getImages().iterator().next().getImageUrl().startsWith("http")) {
//                delete old images
                oldProduct.getImages().forEach(img -> {
                    imageUploadUtils.delete("product", img.getImageUrl());
                });

//                upload new images
                product.getImages().forEach(img -> {
                    String imgFileName = imageUploadUtils.uploadImgBase64("product", img);
                    img.setImageUrl(imgFileName);
                });
            } else {
                product.setImages(oldProduct.getImages());
            }

            List<ProductVariant> variantsToRemove = new ArrayList<>();
            for (ProductVariant oldVar : oldProduct.getProductVariants()) {
                boolean foundInCurrentVariants = false;

                for (ProductVariant var : product.getProductVariants()) {
                    if (oldVar.getProductVariantId() != null && // for new variant
                            oldVar.getProductVariantId().compareTo(var.getProductVariantId()) == 0 // existing variant
                    ) {
                        foundInCurrentVariants = true;
                        break; // Found a matching variant in the current product, no need to check further
                    }
                }

                if (!foundInCurrentVariants) {
                    variantsToRemove.add(oldVar); // Add to the list of variants to be removed
                }
            }

            for (ProductVariant variant : variantsToRemove) {
                if(variant.getImage() != null) {
                    imageUploadUtils.delete("variant", variant.getImage().getImageUrl());
                }
                productVariantService.deleteById(variant.getProductVariantId());
            }

            Set<ProductVariant> updatedVariants = new HashSet<>();
            for (ProductVariant variant : product.getProductVariants()) {
                ProductVariant updatedVariant = updateProductVariant(variant);
                updatedVariant.setProduct(product);
                updatedVariants.add(updatedVariant);
            }
            if(product.getSlug() == null) {
                product.setSlug(createSlug(product.getProductName()));
            }
            product.setProductVariants(new HashSet<>());
            product.getProductVariants().addAll(updatedVariants);
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createSlug(String productName) {
        String slugPart1 = productName.trim().replaceAll("\\s+", "-");
        String uuidPart = UUID.randomUUID().toString().substring(0, 6);
        String finalSlug = slugPart1 + "-" + uuidPart;

        return finalSlug;
    }

    private ProductVariant updateProductVariant(ProductVariant variant) {
//      add new variant
        if(variant.getProductVariantId() == null) {
//            upload new img
            if(variant.getImage() != null) {
                String variantImgFileName = imageUploadUtils.uploadImgBase64("variant", variant.getImage());
                variant.getImage().setImageUrl(variantImgFileName);
            }
        } else {
//          update existing variant
            ProductVariant oldVariant = productVariantService.findById(variant.getProductVariantId());
            if(variant.getImage() != null) {
//               upload new img
                if(!variant.getImage().getImageUrl().startsWith("http")) {
                    if(oldVariant.getImage() != null) {
                        imageUploadUtils.delete("variant", oldVariant.getImage().getImageUrl());
                    }
                    String imgFileName = imageUploadUtils.uploadImgBase64("variant", variant.getImage());
                    variant.getImage().setImageUrl(imgFileName);
                } else {
//                    did not upload new img
                    if(oldVariant.getImage() != null) {
                        variant.setImage(oldVariant.getImage());
                    }
                }
            }
        }

//       insert custom size
        if(variant.getProductSize().getProductSizeId() == null) {
            ProductSize insertedSize = this.productSizeRepository.save(variant.getProductSize());
            variant.setProductSize(insertedSize);
        }
        return variant;
    }

    @Override
    public Boolean updateNewStatus(List<Product> products, boolean new_) {
        try {
            products.stream().forEach(pro -> {
                Product oldProduct = productRepository.findById(pro.getProductId()).get();
                oldProduct.setNew_(new_);
                productRepository.save(oldProduct);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean updateTopStatus(List<Product> products, boolean top) {
        try {
            products.stream().forEach(pro -> {
                Product oldProduct = productRepository.findById(pro.getProductId()).get();
                oldProduct.setTop(top);
                productRepository.save(oldProduct);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean updateActiveStatus(List<Product> products, boolean active) {
        try {
            products.stream().forEach(pro -> {
                Product oldProduct = productRepository.findById(pro.getProductId()).get();
                oldProduct.setActive(active);
                productRepository.save(oldProduct);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean updateProductSale(List<Product> products, ProductSale productSale) {
        try {
            products.stream().forEach(pro -> {
                Product oldProduct = productRepository.findById(pro.getProductId()).get();
                oldProduct.setProductSale(productSale);
                oldProduct.setSale(productSale != null);
                productRepository.save(oldProduct);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean updateStatuses(List<Product> products, boolean new_, boolean top, boolean active, ProductSale productSale) {
        try {
            products.stream().forEach(pro -> {
                Product oldProduct = productRepository.findById(pro.getProductId()).get();
                oldProduct.setActive(active);
                oldProduct.setTop(top);
                oldProduct.setNew_(new_);
                oldProduct.setProductSale(productSale);
                oldProduct.setSale(productSale != null);
                productRepository.save(oldProduct);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean delete(Integer productId) {
        try {
            Product product = this.productRepository.findById(productId).get();
            product.getImages().forEach(image -> {
                imageUploadUtils.delete("product", image.getImageUrl());
            });
            imageUploadUtils.delete("size_guide", product.getImageSizeGuide().getImageUrl());

            product.getProductVariants().forEach(variant -> {
                if (variant.getImage() != null) {
                    imageUploadUtils.delete("variant", variant.getImage().getImageUrl());
                }
            });
            product.getOrderDetails().forEach(orderDetail -> {
                orderDetail.setProduct(null);
                this.orderDetailService.save(orderDetail);
            });
            this.productRepository.deleteById(product.getProductId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(List<Product> products) {
        try {
            products.forEach(pro -> delete(pro.getProductId()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean existById(Integer productId) {
        try {
            return productRepository.existsById(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ProductFindAllDTO> findByNameKeyword(String keyword) {
        try {
            return productRepository.findByProductNameStartingWith(keyword.toLowerCase()).stream()
                    .map(ProductFindAllDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<ProductVariantDTO> findVariantsByProductId(Integer productId) {
        try {
            Product product = productRepository.findById(productId).get();
            return product.getProductVariants().stream()
                    .map(ProductVariantDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<ProductSizeDTO> findSizesByProductId(Integer productId) {
        try {
            Product product = productRepository.findById(productId).get();
            return product.getProductVariants().stream()
                    .map(variant -> variant.getProductSize())
                    .map(ProductSizeDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public BigDecimal findPrice(Integer productId, ProductSize productSize) {
        try {
            Product product = productRepository.findById(productId).get();
            return product.getProductVariants().stream()
                    .filter(variant -> {
                        return variant.getProductSize().getProductSizeId().compareTo(productSize.getProductSizeId()) == 0;})
                    .map(variant -> variant.getPrice())
                    .findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Integer findMaxQuantity(Integer productId, ProductSize productSize, BigDecimal price) {
        try {
            Product product = productRepository.findById(productId).get();
            return product.getProductVariants().stream()
                    .filter(variant -> {
                        return variant.getProductSize().getProductSizeId().compareTo(productSize.getProductSizeId()) == 0 &&
                                variant.getPrice().compareTo(price) == 0;})
                    .map(variant -> variant.getQuantity())
                    .findFirst().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ProductReviewDTO> findProductReviews(Integer productId) {
        try {
            return productReviewService.findByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Integer countTotalComments(Integer productId) {
        try {
            return productRepository.countProductReviewsByProductId(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long count() {
        return productRepository.count();
    }

    @Override
    public List<ProductSizeDTO> findAllSizes() {
        return productSizeRepository.findAll()
                .stream()
                .map(ProductSizeDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductFindAllDTO> findByPages(Pageable pageable, String catalog, String size, String level, String orderBy, String searchTerm) {
        Page<Product> productPage = productRepository
                .findAll(ProductSpecification.withFilters(catalog, size, level, orderBy, searchTerm), pageable);

        List<ProductFindAllDTO> dtoList = productPage
                .stream()
                .map(ProductFindAllDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(mapToFindAll(dtoList), pageable, productPage.getTotalElements());
    }

    @Override
    public Boolean comment(Integer productId, String username, Integer ratingStar, String content) {
        try {
            ProductReview review = new ProductReview();
            review.setAccount(accountRepository.findAccountByUsername(username));
            review.setProduct(productRepository.findById(productId).get());
            review.setRating(ratingStar);
            review.setContent(content);
            review.setCreatedAt(new Date());
            review.setUpdatedAt(new Date());
            productReviewRepository.save(review);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<BigDecimal> findMinAndMaxPrice(Integer productId) {
        Product product = productRepository.findById(productId).get();
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
