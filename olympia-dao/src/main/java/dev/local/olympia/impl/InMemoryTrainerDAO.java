package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.interfaces.MapStorage;
import dev.local.olympia.interfaces.TrainerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryTrainerDAO implements TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryTrainerDAO.class);
    private final MapStorage<Object> mapStorage;

    @Autowired
    public InMemoryTrainerDAO(MapStorage<Object> mapStorage) {
        this.mapStorage = mapStorage;
        logger.info("InMemoryTrainerDAO initialized with MapStorage.");
    }

    @Override
    public Trainer save(Trainer trainer) {
        checkId(trainer.getId());
        logger.debug("Saving trainer with ID: {}", trainer.getId());
        return mapStorage.save(Trainer.class, trainer.getId(), trainer);
    }

    @Override
    public Optional<Trainer> findById(String id) {
        checkId(id);
        logger.debug("Finding trainer with ID: {}", id);
        return mapStorage.findById(Trainer.class, id);
    }

    @Override
    public List<Trainer> findAll() {
        logger.debug("Finding all trainers.");
        return new ArrayList<>(getTrainerStorage().values());
    }

    @Override
    public boolean existsById(String id) {
        checkId(id);
        logger.debug("Checking if trainer with ID {} exists.", id);
        return mapStorage.existsById(Trainer.class, id);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        logger.debug("Finding trainer by username: {}", username);
        return getTrainerStorage().values().stream()
                .filter(t -> t.getUsername() != null && t.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Finding trainers by first name '{}' and last name '{}'.", firstName, lastName);
        return getTrainerStorage().values().stream()
                .filter(t -> t.getFirstName() != null && t.getFirstName().equalsIgnoreCase(firstName) &&
                        t.getLastName() != null && t.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    private Map<String, Trainer> getTrainerStorage() {
        return mapStorage.getStorage(Trainer.class);
    }

    private static void checkId(String id) {
        if (id == null || id.isEmpty()) {
            logger.error("Trainer ID cannot be null or empty for finding.");
            throw new IllegalArgumentException("Trainer ID must be provided.");
        }
    }
}
