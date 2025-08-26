package dev.local.olympia.interfaces;

import dev.local.olympia.domain.TrainingType;

import java.util.List;

public interface TrainingTypeDAO {
    TrainingType save(TrainingType trainingType);

    TrainingType findByName(String name);
    List<TrainingType> findAll();
    int count();
}
