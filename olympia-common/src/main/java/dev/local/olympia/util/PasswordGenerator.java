package dev.local.olympia.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PasswordGenerator.class);
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String OTHER_CHAR = "!@#$%^&*()-_=+"; // Special characters

    private static final String PASSWORD_CHARS = CHAR_LOWER + CHAR_UPPER + DIGIT + OTHER_CHAR;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random password of a specified length.
     * The password will contain a mix of lowercase, uppercase, digits, and special characters.
     *
     * @param length The desired length of the password.
     * @return A randomly generated password string.
     * @throws IllegalArgumentException if length is less than 4 (to ensure at least one of each character type).
     */
    public static String generateRandomPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4 to ensure character diversity.");
        }

        // Ensure at least one of each type
        StringBuilder password = new StringBuilder(length);
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        password.append(DIGIT.charAt(random.nextInt(DIGIT.length())));
        password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        // Fill the rest of the length with random characters from the full set
        IntStream.range(4, length).forEach(i ->
                password.append(PASSWORD_CHARS.charAt(random.nextInt(PASSWORD_CHARS.length())))
        );

        // Shuffle the characters to randomize positions
        List<Character> charList = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(charList);

        String generatedPassword = charList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());

        logger.info("Generated random password of length {}.", length);
        // Do not log the actual password for security reasons in production
        logger.debug("Generated password (debug only): {}", generatedPassword);
        return generatedPassword;
    }
}
