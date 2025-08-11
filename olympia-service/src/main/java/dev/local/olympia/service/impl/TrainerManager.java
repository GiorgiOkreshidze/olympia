package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.User;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingTypeDAO;
import dev.local.olympia.service.interfaces.TrainerService;
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
public class TrainerManager implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerManager.class);
    private static final int PASSWORD_LENGTH = 10;

    private final TrainerDAO trainerDAO;
    private final TrainingTypeDAO trainingTypeDAO;

    @Autowired
    public TrainerManager(TrainerDAO trainerDAO, TrainingTypeDAO trainingTypeDAO) {
        this.trainerDAO = trainerDAO;
        logger.info("TrainerManager initialized with TrainerDAO.");
        this.trainingTypeDAO = trainingTypeDAO;
        logger.info("TrainerManager initialized with TrainingTypeDAO.");
    }


    @Override
    @Transactional
    public Trainer createTrainer(TrainerCreationRequest request) {
        logger.info("Attempting to create new trainer: {} {}", request.getFirstName(), request.getLastName());

        String uniqueUsername = generateUniqueUsername(request.getFirstName(), request.getLastName());
        String randomPassword = PasswordGenerator.generateRandomPassword(PASSWORD_LENGTH);

        var trainingType = trainingTypeDAO.findByName(request.getSpecialization());

        if (trainingType == null) {
            logger.error("Training type '{}' not found.", request.getSpecialization());
            throw new ResourceNotFoundException("Training type '" + request.getSpecialization() + "' not found.");
        }

        Trainer newTrainer = new Trainer(
                request.getFirstName(),
                request.getLastName(),
                uniqueUsername,
                randomPassword,
                trainingType
        );

        Trainer savedTrainer = trainerDAO.save(newTrainer);
        logger.info(
                "Successfully created and saved trainer with ID: {} and Username: {}",
                savedTrainer.getUser().getId(),
                savedTrainer.getUser().getUsername()
        );
        return savedTrainer;
    }

    @Override
    @Transactional
    public Trainer updateTrainer(TrainerUpdateRequest request) {
        logger.info("Attempting to update trainer with ID: {}", request.getId());

        Trainer existingTrainer = trainerDAO.findById(request.getId())
                .orElseThrow(() -> {
                    logger.warn("Trainer with ID {} not found for update.", request.getId());
                    return new ResourceNotFoundException("Trainer with ID " + request.getId() + " not found.");
                });

        var trainingType = trainingTypeDAO.findByName(request.getSpecialization());

        User user = existingTrainer.getUser();

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

        if (request.getSpecialization() != null) {
            existingTrainer.setSpecialization(trainingType);
        }

        Trainer updatedTrainer = trainerDAO.save(existingTrainer);
        logger.info("Successfully updated trainer with ID: {}", updatedTrainer.getUser().getId());
        return updatedTrainer;
    }

    @Override
    @Transactional
    public Trainer updateTrainerPassword(String id, String newPassword) {
        logger.info("Attempting to update password for trainer with ID: {}", id);

        Trainer existingTrainer = trainerDAO.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Trainer with ID {} not found for password update.", id);
                    return new ResourceNotFoundException("Trainer with ID " + id + " not found.");
                });

        existingTrainer.getUser().setPassword(newPassword);
        Trainer updatedTrainer = trainerDAO.save(existingTrainer);
        logger.info("Successfully updated password for trainer with ID: {}", updatedTrainer.getUser().getId());
        return updatedTrainer;
    }

    @Override
    @Transactional
    public Trainer activateDeactivateTrainer(String username, boolean isActive) {
        logger.info("Attempting to {} trainer with username: {}", isActive ? "activate" : "deactivate", username);

        Trainer existingTrainer = trainerDAO.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Trainer with username {} not found for activation/deactivation.", username);
                    return new ResourceNotFoundException("Trainer with username " + username + " not found.");
                });

        existingTrainer.getUser().setActive(isActive);
        Trainer updatedTrainer = trainerDAO.save(existingTrainer);
        logger.info(
                "Successfully {} trainer with username: {}",
                isActive ? "activated" : "deactivated",
                updatedTrainer.getUser().getUsername()

        );
        return updatedTrainer;
    }

    @Override
    @Transactional
    public List<Training> getTrainerTrainingsList(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        logger.debug("Retrieving trainings for trainer: {} from {} to {}", username, fromDate, toDate);

        Trainer trainer = trainerDAO.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Trainer with username {} not found for training retrieval.", username);
                    return new ResourceNotFoundException("Trainer with username " + username + " not found.");
                });

        return trainerDAO.findTrainingsByTrainer(trainer, fromDate, toDate, traineeName);
    }

    @Override
    @Transactional
    public Optional<Trainer> selectTrainerById(String id) {
        logger.debug("Selecting trainer by ID: {}", id);
        return trainerDAO.findById(id);
    }

    @Override
    @Transactional
    public List<Trainer> selectAllTrainers() {
        logger.debug("Selecting all trainers.");
        return trainerDAO.findAll();
    }

    @Override
    @Transactional
    public Optional<Trainer> selectTrainerByUsername(String username) {
        logger.debug("Selecting trainer by username: {}", username);
        return trainerDAO.findByUsername(username);
    }

    private String generateUniqueUsername(String firstName, String lastName){
        String baseUsername = UsernameGenerator.generateBaseUsername(firstName, lastName);
        UsernameGenerator.UsernameExistsChecker checker = username ->
                trainerDAO.findByUsername(username).isPresent();
        return UsernameGenerator.generateUniqueUsername(baseUsername, checker);
    }
}
