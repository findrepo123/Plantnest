package com.project.api.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.dtos.CatalogDTO;
import com.project.api.entities.Catalog;
import com.project.api.entities.Image;
import com.project.api.services.CatalogService;
import com.project.api.utilities.ImageUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/admin/catalogs")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private ImageUploadUtils imageUploadUtils;

    @GetMapping("findAll")
    public ResponseEntity<List<CatalogDTO>> findAll() {
        try {
            return new ResponseEntity<>(this.catalogService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findById/{catalogId}")
    public ResponseEntity<CatalogDTO> findById(@PathVariable Integer catalogId) {
        try {
            return new ResponseEntity<>(this.catalogService.findDTOById(catalogId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> create(
            @RequestParam("catalog") String catalogJson,
            @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Catalog catalog = objectMapper.readValue(catalogJson, Catalog.class);

            Image catalogImg = new Image(imageUploadUtils.upload("catalog", imageFile));
            catalog.setImage(catalogImg);

            this.catalogService.save(catalog);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Create Catalog Failed");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update")
    public ResponseEntity update(
            @RequestParam("catalog") String catalogJson,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Catalog catalog = objectMapper.readValue(catalogJson, Catalog.class);
            Catalog oldCatalog = this.catalogService.findById(catalog.getCatalogId());
            if(imageFile != null) {
                imageUploadUtils.delete("catalog", oldCatalog.getImage().getImageUrl());

                Image catalogImg = new Image(imageUploadUtils.upload("catalog", imageFile));
                catalog.setImage(catalogImg);
            } else {
                Image oldCataImg = oldCatalog.getImage();
                catalog.setImage(oldCataImg);
            }

            this.catalogService.save(catalog);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Update Catalog Failed");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("delete/{catalogId}")
    public ResponseEntity<Boolean> delete(@PathVariable("catalogId") Integer catalogId) {
        try {
            return new ResponseEntity<>(this.catalogService.delete(catalogId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
