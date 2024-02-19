package com.project.api.repositories;

import com.project.api.entities.Role;
import com.project.api.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "wards", path="wards")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface WardRepository extends JpaRepository<Ward, String> {
}
