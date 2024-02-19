package com.project.api.services;

import com.project.api.dtos.PlantingDifficultyLevelDTO;
import com.project.api.entities.PlantingDifficultyLevel;

import java.util.List;

public interface PlantingDifficultyLevelService {
    List<PlantingDifficultyLevelDTO> findAll();
}
