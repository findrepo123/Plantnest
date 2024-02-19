package com.project.api.services;


import com.project.api.dtos.CatalogDTO;
import com.project.api.entities.Catalog;

import java.util.List;

public interface CatalogService {
    List<CatalogDTO> findAll();

    CatalogDTO findDTOById(Integer id);

    Catalog findById(Integer id);

    Catalog save(Catalog category);

    Boolean delete(Integer id);

    Boolean delete(List<Catalog> categories);
    Long count();
}
