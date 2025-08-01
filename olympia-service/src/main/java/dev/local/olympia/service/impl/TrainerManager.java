package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.service.interfaces.TrainerService;
import dev.local.olympia.util.PasswordGenerator;
import dev.local.olympia.util.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerManager implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerManager.class);
    private static final int PASSWORD_LENGTH = 10;

    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerManager(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
        logger.info("TrainerManager initialized with TrainerDAO.");
    }


    @Override
    public Trainer createTrainer(TrainerCreationRequest request) {
        logger.info("Attempting to create new trainer: {} {}", request.getFirstName(), request.getLastName());

        String uniqueUsername = generateUniqueUsername(request.getFirstName(), request.getLastName());
        String randomPassword = PasswordGenerator.generateRandomPassword(PASSWORD_LENGTH);

        Trainer newTrainer = new Trainer(
                request.getFirstName(),
                request.getLastName(),
                uniqueUsername,
                randomPassword,
                request.getSpecialization()
        );

        Trainer savedTrainer = trainerDAO.save(newTrainer);
        logger.info("Successfully created and saved trainer with ID: {} and Username: {}", savedTrainer.getId(), savedTrainer.getUsername());
        return savedTrainer;
    }

    @Override
    public Trainer updateTrainer(TrainerUpdateRequest request) {
        logger.info("Attempting to update trainer with ID: {}", request.getId());

        Trainer existingTrainer = trainerDAO.findById(request.getId())
                .orElseThrow(() -> {
                    logger.warn("Trainer with ID {} not found for update.", request.getId());
                    return new ResourceNotFoundException("Trainer with ID " + request.getId() + " not found.");
                });

        if (request.getFirstName() != null) {
            existingTrainer.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existingTrainer.setLastName(request.getLastName());
        }
        if (request.getFirstName() != null || request.getLastName() != null) {
            existingTrainer.setUsername(generateUniqueUsername(request.getFirstName(), request.getLastName()));
        }
        if (request.getSpecialization() != null) {
            existingTrainer.setSpecialization(request.getSpecialization());
        }
        if (request.getIsActive() != null) {
            existingTrainer.setActive(request.getIsActive());
        }

        Trainer updatedTrainer = trainerDAO.save(existingTrainer);
        logger.info("Successfully updated trainer with ID: {}", updatedTrainer.getId());
        return updatedTrainer;
    }

    @Override
    public Optional<Trainer> selectTrainerById(String id) {
        logger.debug("Selecting trainer by ID: {}", id);
        return trainerDAO.findById(id);
    }

    @Override
    public List<Trainer> selectAllTrainers() {
        logger.debug("Selecting all trainers.");
        return trainerDAO.findAll();
    }

    @Override
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
