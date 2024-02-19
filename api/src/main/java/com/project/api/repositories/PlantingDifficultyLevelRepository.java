package com.project.api.repositories;

import com.project.api.entities.PaymentMethod;
import com.project.api.entities.PlantingDifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "plantingDifficultyLevels", path="planting-difficulty-levels123")
@CrossOrigin({"http://localhost:4200", "http://localhost:4300"})
public interface PlantingDifficultyLevelRepository extends JpaRepository<PlantingDifficultyLevel, Integer> {
}
