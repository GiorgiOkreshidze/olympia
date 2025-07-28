package dev.local.olympia.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsernameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UsernameGenerator.class);

    public static String generateBaseUsername(String firstName, String lastName) {
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

    public static String generateUniqueUsername(String baseUsername, UsernameExistsChecker usernameExistsChecker) {
        if(usernameExistsChecker == null) {
            logger.error("UsernameExistsChecker cannot be null.");
            throw new IllegalArgumentException("UsernameExistsChecker must not be null.");
        }
        String uniqueUsername = baseUsername;
        int suffix = 0;
        // Loop until a unique username is found
        while (usernameExistsChecker.exists(uniqueUsername)) {
            suffix++;
            uniqueUsername = baseUsername + "." + suffix;
            logger.debug("Username '{}' already exists. Trying: '{}'", baseUsername, uniqueUsername);
        }
        logger.info("Generated unique username: {}", uniqueUsername);
        return uniqueUsername;
    }

    /**
     * Functional interface to abstract the logic of checking if a username exists.
     * This allows the generator to be decoupled from the DAO/Service layer.
     */
    @FunctionalInterface
    public interface UsernameExistsChecker {
        boolean exists(String username);
    }
}
