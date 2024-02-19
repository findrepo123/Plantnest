package com.project.api.services.impl;

import com.project.api.dtos.PlantingDifficultyLevelDTO;
import com.project.api.entities.PlantingDifficultyLevel;
import com.project.api.repositories.PlantingDifficultyLevelRepository;
import com.project.api.services.PlantingDifficultyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantingDifficultyLevelServiceImpl implements PlantingDifficultyLevelService {

    @Autowired
    private PlantingDifficultyLevelRepository repository;


    @Override
    public List<PlantingDifficultyLevelDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(PlantingDifficultyLevelDTO::new)
                .collect(Collectors.toList());
    }
}
