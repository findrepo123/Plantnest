package com.project.api.repositories;

import com.project.api.entities.District;
import com.project.api.entities.projection.CouponProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "districts", path="districts")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface DistrictRepository extends JpaRepository<District, String> {
}
