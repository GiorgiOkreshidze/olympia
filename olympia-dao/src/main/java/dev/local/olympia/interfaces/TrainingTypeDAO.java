package dev.local.olympia.interfaces;

import dev.local.olympia.domain.TrainingType;

public interface TrainingTypeDAO {
    TrainingType findByName(String name);
}
