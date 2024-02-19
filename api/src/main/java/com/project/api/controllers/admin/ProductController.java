package com.project.api.controllers.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.dtos.*;
import com.project.api.entities.Product;
import com.project.api.entities.ProductSale;
import com.project.api.entities.ProductSize;
import com.project.api.services.ProductService;
import com.project.api.services.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/admin/products")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping("{productId}")
    public ResponseEntity<ProductDetailDTO> findById(@PathVariable Integer productId) {
        try {
            return new ResponseEntity<>(this.productService.findById(productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{productId}/productReviews")
    public ResponseEntity<List<ProductReviewDTO>> findProductReviews(@PathVariable Integer productId) {
        try {
            return new ResponseEntity<>(this.productService.findProductReviews(productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<List<ProductFindAllDTO>> findAll() {
        try {
            return new ResponseEntity<>(productService.findAllDTO(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update")
    public ResponseEntity<Boolean> update(@RequestBody Product product) {
        try {
            return new ResponseEntity<>(this.productService.update(product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody Product product) {
        try {
            if(this.productService.save(product) != null) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer productId) {
        try {
            if(this.productService.delete(productId)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("delete-products")
    public ResponseEntity<Boolean> delete(@RequestBody List<Product> products) {
        try {
            if(this.productService.delete(products)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-new-status")
    public ResponseEntity<Boolean> updateNewStatus(
            @RequestParam("products") String productsJson,
            @RequestParam("new_") Boolean new_) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(productsJson, new TypeReference<List<Product>>() {});

            if(this.productService.updateNewStatus(products, new_)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-top-status")
    public ResponseEntity<Boolean> updateTopStatus(
            @RequestParam("products") String productsJson,
            @RequestParam("top") Boolean top) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(productsJson, new TypeReference<List<Product>>() {});

            if(this.productService.updateTopStatus(products, top)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-active-status")
    public ResponseEntity<Boolean> updateActiveStatus(
            @RequestParam("products") String productsJson,
            @RequestParam("active") Boolean active) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(productsJson, new TypeReference<List<Product>>() {});

            if(this.productService.updateActiveStatus(products, active)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-sale")
    public ResponseEntity<Boolean> updateNewStatus(
            @RequestParam("products") String productsJson,
            @RequestParam("productSale") String productSaleJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(productsJson, new TypeReference<List<Product>>() {});
            ProductSale productSale = mapper.readValue(productSaleJson, ProductSale.class);

            if(this.productService.updateProductSale(products, productSale)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-statuses")
    public ResponseEntity<Boolean> updateStatuses(
            @RequestParam("products") String productsJson,
            @RequestParam("new_") Boolean new_,
            @RequestParam("active") Boolean active,
            @RequestParam("top") Boolean top,
            @RequestParam("productSale") String productSaleJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> products = mapper.readValue(productsJson, new TypeReference<List<Product>>() {});
            ProductSale productSale = mapper.readValue(productSaleJson, ProductSale.class);

            if(this.productService.updateStatuses(products, new_, top, active, productSale)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("isExist")
    public ResponseEntity<Boolean> isExist(@RequestParam Integer productId) {
        try {
            return new ResponseEntity<>(productService.existById(productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findByNameKeyword")
    public ResponseEntity<List<ProductFindAllDTO>> findByNameKeyword(@RequestParam String keyword) {
        try {
            return new ResponseEntity<>(productService.findByNameKeyword(keyword), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productId}/sizes")
    public ResponseEntity<List<ProductSizeDTO>> findSizesByProductId(@PathVariable Integer productId) {
        try {
            return new ResponseEntity<>(productService.findSizesByProductId(productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariantDTO>> findVariantsByProductId(@PathVariable Integer productId) {
        try {
            return new ResponseEntity<>(productService.findVariantsByProductId(productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("findPrice")
    public ResponseEntity<BigDecimal> findPrice(
            @RequestParam Integer productId,
            @RequestParam String sizeJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductSize productSize = mapper.readValue(sizeJson, ProductSize.class);
            return new ResponseEntity<>(productService.findPrice(productId, productSize), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("findMaxQuantity")
    public ResponseEntity<BigDecimal> findMaxQuantity(
            @RequestParam Integer productId,
            @RequestParam String sizeJson,
            @RequestParam String price) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductSize productSize = mapper.readValue(sizeJson, ProductSize.class);
            BigDecimal productPrice = mapper.readValue(price, BigDecimal.class);
            return new ResponseEntity<>(productService.findPrice(productId, productSize), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{productId}/countComments")
    public ResponseEntity<Integer> countComments(@PathVariable Integer productId) {
        try {
            return new ResponseEntity<>(productService.countTotalComments(productId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("count")
    public ResponseEntity<Long> count() {
        try {
            return new ResponseEntity<>(productService.count(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
