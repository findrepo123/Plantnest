package com.project.api.services.impl;

import com.project.api.dtos.CatalogDTO;
import com.project.api.entities.Catalog;
import com.project.api.repositories.CatalogRepository;
import com.project.api.repositories.ProductRepository;
import com.project.api.services.CatalogService;
import com.project.api.utilities.ImageUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageUploadUtils imageUploadUtils;

    @Override
    public List<CatalogDTO> findAll() {
        return catalogRepository.findCatalogByParentCatalogIsNull().stream()
                .map(CatalogDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Catalog findById(Integer id) {
        return catalogRepository.findById(id).get();
    }

    @Override
    public CatalogDTO findDTOById(Integer id) {
        try {
            Catalog catalog = catalogRepository.findById(id).get();

            CatalogDTO dto = new CatalogDTO(catalog);

            if(catalog.getParentCatalog() != null) dto.setParentCatalog(new CatalogDTO(catalog.getParentCatalog()));
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Catalog save(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    public Boolean delete(Integer id) {
        try {
            Catalog catalog = this.findById(id);
            if(catalog.getParentCatalog() == null) {
                catalog.getChildCatalogs().forEach(this::deleteSingleCatalog);
            }
            catalog.getProducts().stream().forEach(product -> {
                product.setCatalog(null);
                productRepository.save(product);
            });
            deleteSingleCatalog(catalog);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void deleteSingleCatalog(Catalog catalog) {
        this.imageUploadUtils.delete("catalog", catalog.getImage().getImageUrl());
        this.catalogRepository.deleteById(catalog.getCatalogId());
    }

    @Override
    public Boolean delete(List<Catalog> catalogs) {
        try {
            catalogs.stream().forEach(cata -> {
                this.delete(cata.getCatalogId());
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Long count() {
        return catalogRepository.count();
    }
}
