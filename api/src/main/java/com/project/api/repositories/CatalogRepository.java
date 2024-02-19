package com.project.api.repositories;

import com.project.api.entities.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "catalogs", path="catalogs" )
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {

    List<Catalog> findCatalogByParentCatalogIsNull();
}
