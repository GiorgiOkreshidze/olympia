package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;
import dev.local.olympia.interfaces.MapStorage;
import dev.local.olympia.interfaces.TrainingDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTrainingDAO implements TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryTrainingDAO.class);
    private final MapStorage<Object> mapStorage;

    @Autowired
    public InMemoryTrainingDAO(MapStorage<Object> mapStorage) {
        this.mapStorage = mapStorage;
        logger.info("InMemoryTrainingDAO initialized with MapStorage.");
    }

    @Override
    public Training save(Training training) {
        checkId(training.getId());
        logger.debug("Saving training with ID: {}", training.getId());
        return mapStorage.save(Training.class, training.getId(), training);
    }

    @Override
    public Optional<Training> findById(String id) {
        checkId(id);
        logger.debug("Finding training with ID: {}", id);
        return mapStorage.findById(Training.class, id);
    }

    @Override
    public List<Training> findAll() {
        logger.debug("Finding all trainings.");
        return new ArrayList<>(getTrainingStorage().values());
    }

    @Override
    public List<Training> findTrainingsByTrainee(Trainee trainee, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        return List.of();
    }

    private Map<String, Training> getTrainingStorage() {
        return mapStorage.getStorage(Training.class);
    }

    private static void checkId(String id) {
        if (id == null || id.isEmpty()) {
            logger.error("Training ID cannot be null or empty for finding.");
            throw new IllegalArgumentException("Trainer ID must be provided.");
        }
    }
}
