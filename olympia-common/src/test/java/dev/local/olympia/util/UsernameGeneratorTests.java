package dev.local.olympia.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsernameGeneratorTests {
/*
    @Test
    @DisplayName("Should generate base username with dot separator and lowercase")
    void generateBaseUsername_Success() {
        assertEquals("john.doe", UsernameGenerator.generateBaseUsername("John", "Doe"));
        assertEquals("jane.smith", UsernameGenerator.generateBaseUsername("Jane", "smith"));
        assertEquals("alicia.k", UsernameGenerator.generateBaseUsername("Alicia", "K"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if special characters are present in names")
    void generateBaseUsername_SpecialCharacters() {
        assertThrows(IllegalArgumentException.class, () -> UsernameGenerator.generateBaseUsername("John!", "Doe-"));
        assertThrows(IllegalArgumentException.class, () -> UsernameGenerator.generateBaseUsername("Marie-", "Claude'"));
    }

    @Test
    @DisplayName("Should generate unique username without suffix if base is unique")
    void generateUniqueUsername_BaseIsUnique() {
        UsernameGenerator.UsernameExistsChecker checker = username -> false;
        assertEquals("test.user", UsernameGenerator.generateUniqueUsername("test.user", checker));
    }

    @Test
    @DisplayName("Should generate unique username with serial suffix if base exists once")
    void generateUniqueUsername_BaseExistsOnce() {
        UsernameGenerator.UsernameExistsChecker checker = username -> username.equals("john.doe");
        assertEquals("john.doe.1", UsernameGenerator.generateUniqueUsername("john.doe", checker));
    }

    @Test
    @DisplayName("Should generate unique username with increasing serial suffix for multiple duplicates")
    void generateUniqueUsername_MultipleDuplicates() {
        UsernameGenerator.UsernameExistsChecker checker = username -> {
            return username.equals("john.doe") ||
                    username.equals("john.doe.1") ||
                    username.equals("john.doe.2");
        };
        assertEquals("john.doe.3", UsernameGenerator.generateUniqueUsername("john.doe", checker));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null first name")
    void generateBaseUsername_NullFirstName() {
        assertThrows(IllegalArgumentException.class, () -> {
            UsernameGenerator.generateBaseUsername(null, "Doe");
        });
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null last name")
    void generateBaseUsername_NullLastName() {
        assertThrows(IllegalArgumentException.class, () -> {
            UsernameGenerator.generateBaseUsername("John", null);
        });
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for null checker")
    void generateUniqueUsername_NullChecker() {
        assertThrows(IllegalArgumentException.class, () -> {
            UsernameGenerator.generateUniqueUsername("test.user", null);
        });
    }*/
}