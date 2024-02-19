package com.project.api.repositories;

import com.project.api.entities.AdministrativeUnit;
import com.project.api.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "administrativeUnit", path="administrative-units")
public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Integer> {
}
