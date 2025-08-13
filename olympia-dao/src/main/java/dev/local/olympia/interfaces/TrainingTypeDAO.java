package dev.local.olympia.interfaces;

import dev.local.olympia.domain.TrainingType;

public interface TrainingTypeDAO {
    TrainingType save(TrainingType trainingType);

    TrainingType findByName(String name);
    int count();
}
