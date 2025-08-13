package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.User;
import dev.local.olympia.dto.AuthCredentials;
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
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;

    @Autowired
    public TrainerManager(
            TrainerDAO trainerDAO,
            TrainingTypeDAO trainingTypeDAO,
            UsernameGenerator usernameGenerator,
            PasswordGenerator passwordGenerator
    ) {
        this.trainerDAO = trainerDAO;
        logger.info("TrainerManager initialized with TrainerDAO.");
        this.trainingTypeDAO = trainingTypeDAO;
        logger.info("TrainerManager initialized with TrainingTypeDAO.");
        this.usernameGenerator = usernameGenerator;
        logger.info("TrainerManager initialized with UsernameGenerator.");
        this.passwordGenerator = passwordGenerator;
        logger.info("TrainerManager initialized with PasswordGenerator.");
    }


    @Override
    @Transactional
    public Trainer createTrainer(TrainerCreationRequest request) {
        logger.info("Attempting to create new trainer: {} {}", request.getFirstName(), request.getLastName());

        String uniqueUsername = generateUniqueUsername(request.getFirstName(), request.getLastName());
        String randomPassword = passwordGenerator.generateRandomPassword(PASSWORD_LENGTH);

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
    public Trainer updateTrainer(TrainerUpdateRequest request, AuthCredentials credentials) {
        authUser(credentials);

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
    public Trainer updateTrainerPassword(String id, String newPassword, AuthCredentials credentials) {
        authUser(credentials);

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
    public Trainer activateDeactivateTrainer(String username, boolean isActive, AuthCredentials credentials) {
        authUser(credentials);

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
    public boolean authenticateTrainer(String username, String password) {
        logger.debug("Authenticating trainer with username: {}", username);

        Optional<Trainer> trainerOpt = trainerDAO.findByUsername(username);
        if (trainerOpt.isEmpty()) {
            logger.warn("Trainer with username {} not found for authentication.", username);
            return false;
        }

        Trainer trainer = trainerOpt.get();
        boolean isAuthenticated = trainer.getUser().getPassword().equals(password);

        if (isAuthenticated) {
            logger.info("Authentication successful for trainer with username: {}", username);
        } else {
            logger.warn("Authentication failed for trainer with username: {}", username);
        }

        return isAuthenticated;
    }

    @Override
    @Transactional
    public List<Training> getTrainerTrainingsList(String username, LocalDate fromDate, LocalDate toDate, String traineeName, AuthCredentials credentials) {
        authUser(credentials);

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
    public List<Trainer> findUnassignedTrainers(String traineeUsername, AuthCredentials credentials) {
        authUser(credentials);

        logger.debug("Finding unassigned trainers for trainee: {}", traineeUsername);
        return trainerDAO.findUnassignedTrainers(traineeUsername);
    }

    @Override
    @Transactional
    public Optional<Trainer> selectTrainerById(String id, AuthCredentials credentials) {
        authUser(credentials);

        logger.debug("Selecting trainer by ID: {}", id);
        return trainerDAO.findById(id);
    }

    @Override
    @Transactional
    public List<Trainer> selectAllTrainers(AuthCredentials credentials) {
        authUser(credentials);

        logger.debug("Selecting all trainers.");
        return trainerDAO.findAll();
    }

    @Override
    @Transactional
    public Optional<Trainer> selectTrainerByUsername(String username, AuthCredentials credentials) {
        authUser(credentials);

        logger.debug("Selecting trainer by username: {}", username);
        return trainerDAO.findByUsername(username);
    }

    private String generateUniqueUsername(String firstName, String lastName){
        String baseUsername = usernameGenerator.generateBaseUsername(firstName, lastName);
        UsernameGenerator.UsernameExistsChecker checker = username ->
                trainerDAO.findByUsername(username).isPresent();
        return usernameGenerator.generateUniqueUsername(baseUsername, checker);
    }

    private void authUser(AuthCredentials credentials) {
        if(authenticateTrainer(credentials.getUsername(), credentials.getPassword())) {
            logger.info("Authenticated user: {}", credentials.getUsername());
        } else {
            logger.warn("Authentication failed for user: {}", credentials.getUsername());
            throw new ResourceNotFoundException("Authentication failed for user: " + credentials.getUsername());
        }
    }
}
