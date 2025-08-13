// olympia-service/src/test/java/dev/local/olympia/service/impl/TraineeManagerTest.java
package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.dto.AuthCredentials;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeManagerTests {

    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private TrainerDAO trainerDAO;
    @Mock
    private TrainingDAO trainingDAO;
    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TraineeManager traineeManager;

    private TraineeCreationRequest creationRequest;
    private Trainee sampleTrainee;
    private AuthCredentials authCredentials;
    private TraineeUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        creationRequest = new TraineeCreationRequest(
                "Jane",
                "Smith",
                LocalDate.of(1995, 5, 10),
                "456 Oak Ave"
        );

        sampleTrainee = new Trainee(
                "Jane",
                "Smith",
                "jane.smith",
                "password123",
                LocalDate.of(1995, 5, 10),
                "456 Oak Ave"
        );

        authCredentials = new AuthCredentials(
                "jane.smith",
                "password123"
        );

        updateRequest = new TraineeUpdateRequest(
                sampleTrainee.getUser().getId(),
                "JaneUpdated",
                "SmithUpdated",
                LocalDate.of(2000, 5, 10),
                "456 Oak Ave updated",
                true
        );
    }

    @Test
    @DisplayName("Should successfully create a new trainee with unique username and password")
    void createTrainee_Success() {
        // Arrange
        when(usernameGenerator.generateBaseUsername("Jane", "Smith"))
                .thenReturn("jane.smith");
        when(usernameGenerator.generateUniqueUsername(eq("jane.smith"), any(UsernameGenerator.UsernameExistsChecker.class)))
                .thenReturn("jane.smith");
        when(passwordGenerator.generateRandomPassword(10))
                .thenReturn("generatedPwd");

        when(traineeDAO.save(any(Trainee.class)))
                .thenAnswer(invocation -> {
                    Trainee trainee = invocation.getArgument(0);
                    trainee.getUser().setId("test-id");
                    return trainee;
                });

        // Act
        Trainee createdTrainee = traineeManager.createTrainee(creationRequest);

        // Assert
        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getUser().getId());
        assertEquals("Jane", createdTrainee.getUser().getFirstName());
        assertEquals("Smith", createdTrainee.getUser().getLastName());
        assertEquals("jane.smith", createdTrainee.getUser().getUsername());
        assertEquals("generatedPwd", createdTrainee.getUser().getPassword());
        assertTrue(createdTrainee.getUser().isActive());

        verify(usernameGenerator).generateBaseUsername("Jane", "Smith");
        verify(usernameGenerator).generateUniqueUsername(eq("jane.smith"), any(UsernameGenerator.UsernameExistsChecker.class));
        verify(passwordGenerator).generateRandomPassword(10);
        verify(traineeDAO).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Should return trainee when selecting by ID and found")
    void selectTraineeById_Found() {
        when(traineeDAO.findById(sampleTrainee.getUser().getId())).thenReturn(Optional.of(sampleTrainee));
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> result = traineeManager.selectTraineeById(sampleTrainee.getUser().getId(), authCredentials);

        assertTrue(result.isPresent());
        assertEquals(sampleTrainee, result.get());
        verify(traineeDAO, times(1)).findById(sampleTrainee.getUser().getId());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by ID and not found")
    void selectTraineeById_NotFound() {
        when(traineeDAO.findById("nonExistentId")).thenReturn(Optional.empty());
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> result = traineeManager.selectTraineeById("nonExistentId", authCredentials);

        assertTrue(result.isEmpty());
        verify(traineeDAO, times(1)).findById("nonExistentId");
    }

    @Test
    @DisplayName("Should return all trainees when selecting all")
    void selectAllTrainees_Success() {
        List<Trainee> trainees = Arrays.asList(sampleTrainee, new Trainee());
        when(traineeDAO.findAll()).thenReturn(trainees);
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        List<Trainee> result = traineeManager.selectAllTrainees(authCredentials);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(trainees, result);
        verify(traineeDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return trainee when selecting by username and found")
    void selectTraineeByUsername_Found() {
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> result = traineeManager.selectTraineeByUsername(sampleTrainee.getUser().getUsername(), authCredentials);

        assertTrue(result.isPresent());
        assertEquals(sampleTrainee, result.get());
        verify(traineeDAO, times(2)).findByUsername(sampleTrainee.getUser().getUsername());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by username and not found")
    void selectTraineeByUsername_NotFound() {
        when(traineeDAO.findByUsername("nonExistentUsername")).thenReturn(Optional.empty());
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> result = traineeManager.selectTraineeByUsername("nonExistentUsername", authCredentials);

        assertTrue(result.isEmpty());
        verify(traineeDAO, times(1)).findByUsername("nonExistentUsername");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent trainee")
    void updateTrainee_NotFound() {
        when(traineeDAO.findById(updateRequest.getId())).thenReturn(Optional.empty());
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            traineeManager.updateTrainee(updateRequest, authCredentials);
        });

        assertEquals("Trainee with ID " + updateRequest.getId() + " not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).findById(updateRequest.getId());
        verify(traineeDAO, never()).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Should update an existing trainee successfully")
    void updateTrainee_Success() {
        when(traineeDAO.findById(updateRequest.getId())).thenReturn(Optional.of(sampleTrainee));
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));
        when(traineeDAO.save(any(Trainee.class)))
                .thenAnswer(invocation -> {
                    return invocation.getArgument(0);
                });

        Trainee updatedTrainee = traineeManager.updateTrainee(updateRequest, authCredentials);

        assertNotNull(updatedTrainee);
        assertEquals(updateRequest.getId(), updatedTrainee.getUser().getId());
        assertEquals(updateRequest.getFirstName(), updatedTrainee.getUser().getFirstName());
        assertEquals(updateRequest.getLastName(), updatedTrainee.getUser().getLastName());
        assertEquals(updateRequest.getDateOfBirth(), updatedTrainee.getDateOfBirth());
        assertEquals(updateRequest.getAddress(), updatedTrainee.getAddress());
        assertTrue(updatedTrainee.getUser().isActive());

        verify(traineeDAO, times(1)).findById(updateRequest.getId());
        verify(traineeDAO, times(1)).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Should delete an existing trainee successfully")
    void deleteTrainee_Success() {
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));
        doNothing().when(traineeDAO).delete(sampleTrainee);

        traineeManager.deleteTrainee(sampleTrainee.getUser().getUsername(), authCredentials);

        verify(traineeDAO, times(1)).delete(sampleTrainee);
        verify(traineeDAO, times(2)).findByUsername(sampleTrainee.getUser().getUsername());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting a non-existent trainee")
    void deleteTrainee_NotFound() {
        when(traineeDAO.findByUsername(sampleTrainee.getUser().getUsername())).thenReturn(Optional.of(sampleTrainee));

        var nonExistentUsername = "nonExistentUsername";

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            traineeManager.deleteTrainee(nonExistentUsername, authCredentials);
        });

        assertEquals("Trainee with username "+ nonExistentUsername +" not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).findByUsername(sampleTrainee.getUser().getUsername());
        verify(traineeDAO, never()).delete(new Trainee());
    }
}