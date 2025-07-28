package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.interfaces.MapStorage;
import dev.local.olympia.interfaces.TraineeDAO;
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
public class InMemoryTraineeDAO implements TraineeDAO {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryTraineeDAO.class);
    private final MapStorage<Object> mapStorage;

    @Autowired
    public InMemoryTraineeDAO(MapStorage<Object> mapStorage) {
        this.mapStorage = mapStorage;
        logger.info("InMemoryTraineeDAO initialized with MapStorage.");
    }

    @Override
    public Trainee save(Trainee trainee) {
        checkId(trainee.getId());
        logger.debug("Saving trainee with ID: {}", trainee.getId());
        return mapStorage.save(Trainee.class, trainee.getId(), trainee);
    }

    @Override
    public Optional<Trainee> findById(String id) {
        checkId(id);
        logger.debug("Finding trainee with ID: {}", id);
        return mapStorage.findById(Trainee.class, id);
    }

    @Override
    public List<Trainee> findAll() {
        logger.debug("Finding all trainees.");
        return new ArrayList<>(getTraineeStorage().values());
    }

    @Override
    public void delete(String id) {
        checkId(id);
        logger.debug("Deleting trainee with ID: {}", id);
        mapStorage.delete(Trainee.class, id);
    }

    @Override
    public boolean existsById(String id) {
        logger.debug("Checking if trainee with ID {} exists.", id);
        return mapStorage.existsById(Trainee.class, id);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        logger.debug("Finding trainee by username: {}", username);
        return getTraineeStorage().values().stream()
                .filter(t -> t.getUsername() != null && t.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Finding trainees by first name '{}' and last name '{}'.", firstName, lastName);
        return getTraineeStorage().values().stream()
                .filter(t -> t.getFirstName() != null && t.getFirstName().equalsIgnoreCase(firstName) &&
                        t.getLastName() != null && t.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    private Map<String, Trainee> getTraineeStorage() {
        return mapStorage.getStorage(Trainee.class);
    }

    private static void checkId(String id) {
        if (id == null || id.isEmpty()) {
            logger.error("Trainee ID cannot be null or empty for finding.");
            throw new IllegalArgumentException("Trainee ID must be provided.");
        }
    }
}
