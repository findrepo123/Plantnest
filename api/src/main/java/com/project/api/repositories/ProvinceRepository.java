package com.project.api.repositories;

import com.project.api.entities.ProductVariant;
import com.project.api.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "provinces", path="provinces")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface ProvinceRepository extends JpaRepository<Province, String> {
}
