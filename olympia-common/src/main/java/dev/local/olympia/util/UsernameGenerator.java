package dev.local.olympia.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsernameGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UsernameGenerator.class);

    public static String generateBaseUsername(String firstName, String lastName) {
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            logger.warn("Attempted to generate username with null or empty first/last name. Using default.");
            return "unknown.user";
        }

        String base = (firstName.trim() + "." + lastName.trim()).toLowerCase();
        logger.debug("Generated base username: {}", base);
        return base;
    }
}
