package dev.local.olympia.util;

import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UsernameGenerator.class);

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    public UsernameGenerator(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    public String generateBaseUsername(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            logger.warn("Attempted to generate username with null or empty first/last name. Using default.");
            throw new IllegalArgumentException("First name and last name must not be null or empty.");
        }

        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            logger.error("Invalid characters in names. Only alphabetic characters are allowed.");
            throw new IllegalArgumentException("Names must only contain alphabetic characters.");
        }

        String base = (firstName.trim() + "." + lastName.trim()).toLowerCase();
        logger.debug("Generated base username: {}", base);
        return base;
    }

    public String generateUniqueUsername(String baseUsername) {
        String uniqueUsername = baseUsername;
        int suffix = 0;
        // Loop until a unique username is found
        while (usernameExists(uniqueUsername)) {
            suffix++;
            uniqueUsername = baseUsername + "." + suffix;
            logger.debug("Username '{}' already exists. Trying: '{}'", baseUsername, uniqueUsername);
        }
        logger.info("Generated unique username: {}", uniqueUsername);
        return uniqueUsername;
    }

    private boolean usernameExists(String username) {
        return traineeDAO.findByUsername(username).isPresent() ||
                trainerDAO.findByUsername(username).isPresent();
    }
}
