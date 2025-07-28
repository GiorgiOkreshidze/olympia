package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.util.PasswordGenerator;
import dev.local.olympia.util.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeManager implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeManager.class);
    private static final int PASSWORD_LENGTH = 10;

    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeManager(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
        logger.info("TraineeManager initialized with TraineeDAO.");
    }

    @Override
    public Trainee createTrainee(TraineeCreationRequest request) {
        logger.info("Attempting to create new trainee: {} {}", request.getFirstName(), request.getLastName());

        String uniqueUsername = generateUniqueUsername(request.getFirstName(), request.getLastName());
        String randomPassword = PasswordGenerator.generateRandomPassword(PASSWORD_LENGTH);

        Trainee newTrainee = new Trainee(
                request.getFirstName(),
                request.getLastName(),
                uniqueUsername,
                randomPassword,
                request.getDateOfBirth(),
                request.getAddress()
        );

        Trainee savedTrainee = traineeDAO.save(newTrainee);
        logger.info("Successfully created and saved trainee with ID: {} and Username: {}", savedTrainee.getId(), savedTrainee.getUsername());
        return savedTrainee;
    }

    @Override
    public Trainee updateTrainee(TraineeUpdateRequest request) {
        logger.info("Attempting to update trainee with ID: {}", request.getId());

        Trainee existingTrainee = traineeDAO.findById(request.getId())
                .orElseThrow(() -> {
                    logger.warn("Trainee with ID {} not found for update.", request.getId());
                    return new ResourceNotFoundException("Trainee with ID " + request.getId() + " not found.");
                });

        if (request.getFirstName() != null) {
            existingTrainee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existingTrainee.setLastName(request.getLastName());
        }
        if (request.getFirstName() != null || request.getLastName() != null) {
            existingTrainee.setUsername(generateUniqueUsername(request.getFirstName(), request.getLastName()));
        }
        if (request.getDateOfBirth() != null) {
            existingTrainee.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            existingTrainee.setAddress(request.getAddress());
        }
        if (request.getIsActive() != null) {
            existingTrainee.setActive(request.getIsActive());
        }

        Trainee updatedTrainee = traineeDAO.save(existingTrainee);
        logger.info("Successfully updated trainee with ID: {}", updatedTrainee.getId());
        return updatedTrainee;
    }

    @Override
    public void deleteTrainee(String id) {
        logger.info("Attempting to delete trainee with ID: {}", id);
        if (!traineeDAO.existsById(id)) {
            logger.warn("Trainee with ID {} not found for deletion.", id);
            throw new ResourceNotFoundException("Trainee with ID " + id + " not found for deletion.");
        }
        traineeDAO.delete(id);
        logger.info("Successfully deleted trainee with ID: {}", id);
    }

    @Override
    public Optional<Trainee> selectTraineeById(String id) {
        logger.debug("Selecting trainee by ID: {}", id);
        return traineeDAO.findById(id);
    }

    @Override
    public List<Trainee> selectAllTrainees() {
        logger.debug("Selecting all trainees.");
        return traineeDAO.findAll();
    }

    @Override
    public Optional<Trainee> selectTraineeByUsername(String username) {
        logger.debug("Selecting trainee by username: {}", username);
        return traineeDAO.findByUsername(username);
    }

    private String generateUniqueUsername(String firstName, String lastName){
        String baseUsername = UsernameGenerator.generateBaseUsername(firstName, lastName);
        UsernameGenerator.UsernameExistsChecker checker = username ->
                traineeDAO.findByUsername(username).isPresent();
        return UsernameGenerator.generateUniqueUsername(baseUsername, checker);
    }
}
