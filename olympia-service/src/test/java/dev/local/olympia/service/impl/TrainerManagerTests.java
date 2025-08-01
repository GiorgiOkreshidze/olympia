package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.util.PasswordGenerator;
import dev.local.olympia.util.UsernameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerManagerTests {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerManager trainerManager;

    private Trainer sampleTrainer;
    private TrainerCreationRequest creationRequest;
    private TrainerUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        sampleTrainer = new Trainer("Alice", "Johnson", "Alice.Johnson", "randomPass", "Yoga");
        creationRequest = new TrainerCreationRequest("Bob", "Williams", "Strength");
        updateRequest = new TrainerUpdateRequest(sampleTrainer.getId(), "Alicia", null, "Advanced Yoga", false);
    }

    @Test
    @DisplayName("Should successfully create a new trainer with unique username and password")
    void createTrainer_Success() {
        try (MockedStatic<UsernameGenerator> mockedUsernameGenerator = mockStatic(UsernameGenerator.class);
             MockedStatic<PasswordGenerator> mockedPasswordGenerator = mockStatic(PasswordGenerator.class)) {

            mockedUsernameGenerator.when(() -> UsernameGenerator.generateBaseUsername(anyString(), anyString()))
                    .thenReturn("bob.williams");
            mockedUsernameGenerator.when(() -> UsernameGenerator.generateUniqueUsername(eq("bob.williams"),
                            any(UsernameGenerator.UsernameExistsChecker.class)))
                    .thenReturn("bob.williams");
            mockedPasswordGenerator.when(() -> PasswordGenerator.generateRandomPassword(anyInt()))
                    .thenReturn("genPassword");

            when(trainerDAO.save(any(Trainer.class))).thenAnswer(invocation -> {
                return invocation.<Trainer>getArgument(0);
            });

            Trainer createdTrainer = trainerManager.createTrainer(creationRequest);

            assertNotNull(createdTrainer);
            assertNotNull(createdTrainer.getId());
            assertEquals("Bob", createdTrainer.getFirstName());
            assertEquals("Williams", createdTrainer.getLastName());
            assertEquals("bob.williams", createdTrainer.getUsername());
            assertEquals("genPassword", createdTrainer.getPassword());
            assertTrue(createdTrainer.isActive());
            assertEquals("Strength", createdTrainer.getSpecialization());

            verify(trainerDAO, times(1)).save(any(Trainer.class));
            mockedUsernameGenerator.verify(() -> UsernameGenerator.generateUniqueUsername(eq("bob.williams"),
                    any(UsernameGenerator.UsernameExistsChecker.class)), times(1));
            mockedPasswordGenerator.verify(() -> PasswordGenerator.generateRandomPassword(10), times(1));
        }
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent trainer")
    void updateTrainer_NotFound() {
        when(trainerDAO.findById(updateRequest.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainerManager.updateTrainer(updateRequest);
        });

        assertEquals("Trainer with ID " + updateRequest.getId() + " not found.", thrown.getMessage());
        verify(trainerDAO, times(1)).findById(updateRequest.getId());
        verify(trainerDAO, never()).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Should return trainer when selecting by ID and found")
    void selectTrainerById_Found() {
        when(trainerDAO.findById(sampleTrainer.getId())).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> result = trainerManager.selectTrainerById(sampleTrainer.getId());

        assertTrue(result.isPresent());
        assertEquals(sampleTrainer, result.get());
        verify(trainerDAO, times(1)).findById(sampleTrainer.getId());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by ID and not found")
    void selectTrainerById_NotFound() {
        when(trainerDAO.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerManager.selectTrainerById("nonExistentId");

        assertTrue(result.isEmpty());
        verify(trainerDAO, times(1)).findById("nonExistentId");
    }

    @Test
    @DisplayName("Should return all trainers when selecting all")
    void selectAllTrainers_Success() {
        List<Trainer> trainers = Arrays.asList(sampleTrainer, new Trainer());
        when(trainerDAO.findAll()).thenReturn(trainers);

        List<Trainer> result = trainerManager.selectAllTrainers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(trainers, result);
        verify(trainerDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return trainer when selecting by username and found")
    void selectTrainerByUsername_Found() {
        when(trainerDAO.findByUsername(sampleTrainer.getUsername())).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> result = trainerManager.selectTrainerByUsername(sampleTrainer.getUsername());

        assertTrue(result.isPresent());
        assertEquals(sampleTrainer, result.get());
        verify(trainerDAO, times(1)).findByUsername(sampleTrainer.getUsername());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by username and not found")
    void selectTrainerByUsername_NotFound() {
        when(trainerDAO.findByUsername("nonExistentUsername")).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerManager.selectTrainerByUsername("nonExistentUsername");

        assertTrue(result.isEmpty());
        verify(trainerDAO, times(1)).findByUsername("nonExistentUsername");
    }
}