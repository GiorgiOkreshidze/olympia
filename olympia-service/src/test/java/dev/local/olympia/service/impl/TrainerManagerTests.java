package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.dto.AuthCredentials;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingTypeDAO;
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
    @Mock
    private TrainingTypeDAO trainingTypeDAO;
    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TrainerManager trainerManager;

    private TrainerCreationRequest creationRequest;
    private Trainer sampleTrainer;
    private TrainingType trainingType;
    private AuthCredentials authCredentials;
    private TrainerUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType("Yoga");

        creationRequest = new TrainerCreationRequest(
                "Bob",
                "Williams",
                "Yoga"
        );

        sampleTrainer = new Trainer(
                "Bob",
                "Williams",
                "bob.williams",
                "password123",
                trainingType
        );

        authCredentials = new AuthCredentials(
                "bob.williams",
                "password123"
        );

        updateRequest = new TrainerUpdateRequest(
                sampleTrainer.getUser().getId(),
                "BobUpdated",
                "WilliamsUpdated",
                "Yoga",
                true
        );
    }

    @Test
    @DisplayName("Should successfully create a new trainer with unique username and password")
    void createTrainer_Success() {
        // Arrange
        when(usernameGenerator.generateBaseUsername("Bob", "Williams"))
                .thenReturn("bob.williams");
        when(usernameGenerator.generateUniqueUsername(eq("bob.williams"), any(UsernameGenerator.UsernameExistsChecker.class)))
                .thenReturn("bob.williams");
        when(passwordGenerator.generateRandomPassword(10))
                .thenReturn("generatedPwd");

        when(trainerDAO.save(any(Trainer.class)))
                .thenAnswer(invocation -> {
                    Trainer trainer = invocation.getArgument(0);
                    trainer.getUser().setId("test-id");
                    return trainer;
                });

        when(trainingTypeDAO.findByName(creationRequest.getSpecialization())).thenReturn(trainingType);

        // Act
        Trainer createdTrainer = trainerManager.createTrainer(creationRequest);

        // Assert
        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getUser().getId());
        assertEquals("Bob", createdTrainer.getUser().getFirstName());
        assertEquals("Williams", createdTrainer.getUser().getLastName());
        assertEquals("bob.williams", createdTrainer.getUser().getUsername());
        assertEquals("generatedPwd", createdTrainer.getUser().getPassword());
        assertEquals(trainingType, createdTrainer.getSpecialization());
        assertTrue(createdTrainer.getUser().isActive());

        verify(usernameGenerator).generateBaseUsername("Bob", "Williams");
        verify(usernameGenerator).generateUniqueUsername(eq("bob.williams"), any(UsernameGenerator.UsernameExistsChecker.class));
        verify(passwordGenerator).generateRandomPassword(10);
        verify(trainerDAO).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Should return trainer when selecting by ID and found")
    void selectTrainerById_Found() {
        when(trainerDAO.findById(sampleTrainer.getUser().getId())).thenReturn(Optional.of(sampleTrainer));
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> result = trainerManager.selectTrainerById(sampleTrainer.getUser().getId(), authCredentials);

        assertTrue(result.isPresent());
        assertEquals(sampleTrainer, result.get());
        verify(trainerDAO, times(1)).findById(sampleTrainer.getUser().getId());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by ID and not found")
    void selectTrainerById_NotFound() {
        when(trainerDAO.findById("nonExistentId")).thenReturn(Optional.empty());
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> result = trainerManager.selectTrainerById("nonExistentId", authCredentials);

        assertTrue(result.isEmpty());
        verify(trainerDAO, times(1)).findById("nonExistentId");
    }

    @Test
    @DisplayName("Should return all trainers when selecting all")
    void selectAllTrainers_Success() {
        List<Trainer> trainers = Arrays.asList(sampleTrainer, new Trainer());
        when(trainerDAO.findAll()).thenReturn(trainers);
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));

        List<Trainer> result = trainerManager.selectAllTrainers(authCredentials);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(trainers, result);
        verify(trainerDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return trainer when selecting by username and found")
    void selectTrainerByUsername_Found() {
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> result = trainerManager.selectTrainerByUsername(sampleTrainer.getUser().getUsername(), authCredentials);

        assertTrue(result.isPresent());
        assertEquals(sampleTrainer, result.get());
        verify(trainerDAO, times(2)).findByUsername(sampleTrainer.getUser().getUsername());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by username and not found")
    void selectTrainerByUsername_NotFound() {
        when(trainerDAO.findByUsername("nonExistentUsername")).thenReturn(Optional.empty());
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));

        Optional<Trainer> result = trainerManager.selectTrainerByUsername("nonExistentUsername", authCredentials);

        assertTrue(result.isEmpty());
        verify(trainerDAO, times(1)).findByUsername("nonExistentUsername");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent trainer")
    void updateTrainer_NotFound() {
        when(trainerDAO.findById(updateRequest.getId())).thenReturn(Optional.empty());
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainerManager.updateTrainer(updateRequest, authCredentials);
        });

        assertEquals("Trainer with ID " + updateRequest.getId() + " not found.", thrown.getMessage());
        verify(trainerDAO, times(1)).findById(updateRequest.getId());
        verify(trainerDAO, never()).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Should update an existing trainee successfully")
    void updateTrainee_Success() {
        when(trainerDAO.findById(updateRequest.getId())).thenReturn(Optional.of(sampleTrainer));
        when(trainerDAO.findByUsername(sampleTrainer.getUser().getUsername())).thenReturn(Optional.of(sampleTrainer));
        when(trainerDAO.save(any(Trainer.class)))
                .thenAnswer(invocation -> {
                    Trainer trainer = invocation.getArgument(0);
                    trainer.setSpecialization(trainingType);
                    return trainer;
                });

        Trainer updatedTrainer = trainerManager.updateTrainer(updateRequest, authCredentials);

        assertNotNull(updatedTrainer);
        assertEquals(updateRequest.getId(), updatedTrainer.getUser().getId());
        assertEquals(updateRequest.getFirstName(), updatedTrainer.getUser().getFirstName());
        assertEquals(updateRequest.getLastName(), updatedTrainer.getUser().getLastName());
        assertEquals(updateRequest.getSpecialization(), updatedTrainer.getSpecialization().getTrainingTypeName());
        assertTrue(updatedTrainer.getUser().isActive());

        verify(trainerDAO, times(1)).findById(updateRequest.getId());
        verify(trainerDAO, times(1)).save(any(Trainer.class));
    }
}