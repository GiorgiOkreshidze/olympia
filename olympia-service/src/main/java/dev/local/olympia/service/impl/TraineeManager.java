package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.User;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.util.PasswordGenerator;
import dev.local.olympia.util.UsernameGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TraineeManager implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeManager.class);
    private static final int PASSWORD_LENGTH = 10;

    private final TraineeDAO traineeDAO;
    private final TrainingDAO trainingDAO;

    @Autowired
    public TraineeManager(TraineeDAO traineeDAO, TrainingDAO trainingDAO) {
        this.traineeDAO = traineeDAO;
        logger.info("TraineeManager initialized with TraineeDAO.");
        this.trainingDAO = trainingDAO;
        logger.info("TraineeManager initialized with TrainingDAO.");
    }

    @Override
    @Transactional
    public Trainee createTrainee(TraineeCreationRequest request) {
        logger.info("Attempting to create new trainee: {} {}",
                request.getFirstName(), request.getLastName()
        );

        String uniqueUsername = generateUniqueUsername(
                request.getFirstName(),
                request.getLastName()
        );
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
        logger.info(
                "Successfully created and saved trainee with ID: {} and Username: {}",
                savedTrainee.getUser().getId(),
                savedTrainee.getUser().getUsername()
        );
        return savedTrainee;
    }

    @Override
    @Transactional
    public Trainee updateTrainee(TraineeUpdateRequest request) {
        logger.info("Attempting to update trainee with ID: {}", request.getId());

        Trainee existingTrainee = traineeDAO.findById(request.getId())
                .orElseThrow(() -> {
                    logger.warn("Trainee with ID {} not found for update.", request.getId());
                    return new ResourceNotFoundException("Trainee with ID " + request.getId() + " not found.");
                });

        User user = existingTrainee.getUser();

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getFirstName() != null || request.getLastName() != null) {
            user.setUsername(generateUniqueUsername(request.getFirstName(), request.getLastName()));
        }
        if (request.getIsActive() != null) {
            user.setActive(request.getIsActive());
        }
        if (request.getDateOfBirth() != null) {
            existingTrainee.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAddress() != null) {
            existingTrainee.setAddress(request.getAddress());
        }

        Trainee updatedTrainee = traineeDAO.save(existingTrainee);
        logger.info("Successfully updated trainee with ID: {}", updatedTrainee.getUser().getId());
        return updatedTrainee;
    }

    @Override
    @Transactional
    public Trainee updateTraineePassword(String id, String newPassword) {
        logger.info("Attempting to update trainee password with ID: {}", id);
        Trainee existingTrainee = traineeDAO.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainee with ID {} not found for password update.", id);
                    return new ResourceNotFoundException("Trainee with ID " + id + " not found.");
                });

        existingTrainee.getUser().setPassword(newPassword);
        Trainee updatedTrainee = traineeDAO.save(existingTrainee);
        logger.info("Successfully updated password for trainee with ID: {}", updatedTrainee.getUser().getId());
        return updatedTrainee;
    }

    @Override
    @Transactional
    public Trainee activateDeactivateTrainee(String username, boolean isActive) {
        logger.info("Attempting to {} trainee with username: {}", isActive ? "activate" : "deactivate", username);
        Trainee existingTrainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Trainee with username {} not found for activation/deactivation.", username);
                    return new ResourceNotFoundException("Trainee with username " + username + " not found.");
                });

        existingTrainee.getUser().setActive(isActive);
        Trainee updatedTrainee = traineeDAO.save(existingTrainee);
        logger.info("Successfully {}d trainee with username: {}", isActive ? "activate" :
                "deactivate", updatedTrainee.getUser().getUsername());

        return updatedTrainee;
    }

    @Override
    @Transactional
    public boolean authenticateTrainee(String username, String password) {
        logger.info("Authenticating trainee with username: {}", username);
        Optional<Trainee> traineeOpt = traineeDAO.findByUsername(username);

        if (traineeOpt.isEmpty()) {
            logger.warn("Trainee with username {} not found for authentication.", username);
            return false;
        }

        Trainee trainee = traineeOpt.get();
        boolean isAuthenticated = trainee.getUser().getPassword().equals(password);

        if (isAuthenticated) {
            logger.info("Authentication successful for trainee with username: {}", username);
        } else {
            logger.warn("Authentication failed for trainee with username: {}", username);
        }

        return isAuthenticated;
    }

    @Override
    @Transactional
    public void deleteTrainee(String username) {
        logger.info("Attempting to delete trainee with username: {}", username);

        var trainee = traineeDAO.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Trainee with username {} not found for deletion.", username);
                    return new ResourceNotFoundException("Trainee with username " + username + " not found.");
                });

        traineeDAO.delete(trainee);
        logger.info("Successfully deleted trainee with username: {}", username);
    }

    @Override
    @Transactional
    public List<Training> getTrainingsByTrainee(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        logger.info("Retrieving trainings for trainee with username: {} from {} to {} by trainer: {} of type: {}",
                username, fromDate, toDate, trainerName, trainingType);

        Optional<Trainee> traineeOpt = traineeDAO.findByUsername(username);
        if (traineeOpt.isEmpty()) {
            logger.warn("Trainee with username {} not found for training retrieval.", username);
            return List.of();
        }

        List<Training> trainings = trainingDAO.findTrainingsByTrainee(traineeOpt.get(), fromDate, toDate, trainerName, trainingType);
        logger.info("Found {} trainings for trainee with username: {}", trainings.size(), username);
        return trainings;
    }

    @Override
    @Transactional
    public Optional<Trainee> selectTraineeById(String id) {
        logger.debug("Selecting trainee by ID: {}", id);
        return traineeDAO.findById(id);
    }

    @Override
    @Transactional
    public List<Trainee> selectAllTrainees() {
        logger.debug("Selecting all trainees.");
        return traineeDAO.findAll();
    }

    @Override
    @Transactional
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
