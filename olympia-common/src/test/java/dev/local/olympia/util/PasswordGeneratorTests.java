package dev.local.olympia.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTests {
/*
    @Test
    @DisplayName("Should generate a password of the specified length")
    void generateRandomPassword_CorrectLength() {
        String password = PasswordGenerator.generateRandomPassword(10);
        assertNotNull(password);
        assertEquals(10, password.length());

        password = PasswordGenerator.generateRandomPassword(5);
        assertEquals(5, password.length());

        password = PasswordGenerator.generateRandomPassword(20);
        assertEquals(20, password.length());
    }

    @Test
    @DisplayName("Should generate different passwords on successive calls")
    void generateRandomPassword_DifferentPasswords() {
        String pass1 = PasswordGenerator.generateRandomPassword(10);
        String pass2 = PasswordGenerator.generateRandomPassword(10);
        assertNotEquals(pass1, pass2);
    }

    @Test
    @DisplayName("Generated password should contain alphanumeric characters")
    void generateRandomPassword_AlphanumericContent() {
        String password = PasswordGenerator.generateRandomPassword(50);
        assertNotNull(password);
        assertTrue(password.matches(".*[a-zA-Z].*"));
        assertTrue(password.matches(".*[0-9].*"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for length less than 1")
    void generateRandomPassword_InvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generateRandomPassword(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generateRandomPassword(-5);
        });
    }*/
}
