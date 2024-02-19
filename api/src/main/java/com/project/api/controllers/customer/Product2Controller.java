package com.project.api.controllers.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.constants.PaginationConstant;
import com.project.api.dtos.*;
import com.project.api.entities.ProductSize;
import com.project.api.services.CatalogService;
import com.project.api.services.PlantingDifficultyLevelService;
import com.project.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class Product2Controller {

    private ProductService productService;
    private CatalogService catalogService;
    private PlantingDifficultyLevelService levelService;

    @Autowired
    public Product2Controller(ProductService productService, CatalogService catalogService, PlantingDifficultyLevelService levelService) {
        this.productService = productService;
        this.catalogService = catalogService;
        this.levelService = levelService;
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductDetailDTO> findById(@PathVariable Integer productId) {
        try {
            return new ResponseEntity<>(this.productService.findById(productId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findBySlug")
    public ResponseEntity<ProductDetailDTO> findBySlug(@RequestParam("slug") String slug) {
        try {
            return new ResponseEntity<>(this.productService.findBySlug(slug), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("find10SaleProduct")
    public ResponseEntity<List<ProductFindAllDTO>> find10SaleProduct() {
        try {
            return new ResponseEntity<>(productService.findTop10SaleProducts(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findTop10Product")
    public ResponseEntity<List<ProductFindAllDTO>> findTop10Product() {
        try {
            return new ResponseEntity<>(productService.findTop10Products(), HttpStatus.OK);
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

    @GetMapping("findByPages")
    public ResponseEntity<Page<ProductFindAllDTO>> findByPages(
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "catalog", required = false) String catalog,
            @RequestParam(name = "size", required = false) String size,
            @RequestParam(name = "level", required = false) String level,
            @RequestParam(name = "orderBy", required = false) String orderBy) {
            page = page == "" || page == null ? PaginationConstant.PAGE_DEFAULT : page;
            pageSize = pageSize == "" || pageSize == null ? PaginationConstant.PAGE_SIZE_DEFAULT : pageSize;

            Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(pageSize));
        return new ResponseEntity<>(
                productService.findByPages(pageable, catalog, size, level, orderBy, searchTerm), HttpStatus.OK);
    }

    @GetMapping("findAll")
    public ResponseEntity<List<ProductFindAllDTO>> findAll() {
        try {
            return new ResponseEntity<>(productService.findAllDTO(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAllCatalogs")
    public ResponseEntity<List<CatalogDTO>> findAllCatalogs() {
        try {
            return new ResponseEntity<>(this.catalogService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAllLevels")
    public ResponseEntity<List<PlantingDifficultyLevelDTO>> findAllLevels() {
        try {
            return new ResponseEntity<>(this.levelService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findAllSizes")
    public ResponseEntity<List<ProductSizeDTO>> findAllSizes() {
        try {
            return new ResponseEntity<>(this.productService.findAllSizes(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    @PostMapping("comment")
    public ResponseEntity<Boolean> comment(
            @RequestParam String username,
            @RequestParam Integer productId,
            @RequestParam Integer ratingStar,
            @RequestParam String content) {
        return new ResponseEntity<>(productService.comment(productId, username, ratingStar, content), HttpStatus.OK);
    }
}
