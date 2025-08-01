// olympia-service/src/test/java/dev/local/olympia/service/impl/TraineeManagerTest.java
package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
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

    @InjectMocks
    private TraineeManager traineeManager;

    private Trainee sampleTrainee;
    private TraineeCreationRequest creationRequest;
    private TraineeUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        sampleTrainee = new Trainee("John", "Doe", "John.Doe", "randomPass", LocalDate.of(1990, 1, 1), "123 Main St");
        creationRequest = new TraineeCreationRequest("Jane", "Smith", LocalDate.of(1995, 5, 10), "456 Oak Ave");
        updateRequest = new TraineeUpdateRequest(sampleTrainee.getId(), "Johnny", null, null, "New Address", true);
    }

    @Test
    @DisplayName("Should successfully create a new trainee with unique username and password")
    void createTrainee_Success() {
        try (MockedStatic<UsernameGenerator> mockedUsernameGenerator = mockStatic(UsernameGenerator.class);
             MockedStatic<PasswordGenerator> mockedPasswordGenerator = mockStatic(PasswordGenerator.class)) {

            mockedUsernameGenerator.when(() -> UsernameGenerator.generateBaseUsername(anyString(), anyString()))
                    .thenReturn("jane.smith");
            mockedUsernameGenerator.when(() -> UsernameGenerator.generateUniqueUsername(eq("jane.smith"), any(UsernameGenerator.UsernameExistsChecker.class)))
                    .thenReturn("jane.smith");
            mockedPasswordGenerator.when(() -> PasswordGenerator.generateRandomPassword(anyInt()))
                    .thenReturn("generatedPwd");

            when(traineeDAO.save(any(Trainee.class))).thenAnswer(invocation -> {
                return invocation.<Trainee>getArgument(0);
            });

            Trainee createdTrainee = traineeManager.createTrainee(creationRequest);

            assertNotNull(createdTrainee);
            assertNotNull(createdTrainee.getId());
            assertEquals("Jane", createdTrainee.getFirstName());
            assertEquals("Smith", createdTrainee.getLastName());
            assertEquals("jane.smith", createdTrainee.getUsername());
            assertEquals("generatedPwd", createdTrainee.getPassword());
            assertTrue(createdTrainee.isActive());

            verify(traineeDAO, times(1)).save(any(Trainee.class));

            mockedUsernameGenerator.verify(() -> UsernameGenerator.generateBaseUsername("Jane", "Smith"), times(1));
            mockedUsernameGenerator.verify(() -> UsernameGenerator.generateUniqueUsername(eq("jane.smith"), any(UsernameGenerator.UsernameExistsChecker.class)), times(1));
            mockedPasswordGenerator.verify(() -> PasswordGenerator.generateRandomPassword(10), times(1));
        }
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating a non-existent trainee")
    void updateTrainee_NotFound() {
        when(traineeDAO.findById(updateRequest.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            traineeManager.updateTrainee(updateRequest);
        });

        assertEquals("Trainee with ID " + updateRequest.getId() + " not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).findById(updateRequest.getId());
        verify(traineeDAO, never()).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Should delete an existing trainee successfully")
    void deleteTrainee_Success() {
        when(traineeDAO.existsById(sampleTrainee.getId())).thenReturn(true);
        doNothing().when(traineeDAO).delete(sampleTrainee.getId());

        traineeManager.deleteTrainee(sampleTrainee.getId());

        verify(traineeDAO, times(1)).existsById(sampleTrainee.getId());
        verify(traineeDAO, times(1)).delete(sampleTrainee.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting a non-existent trainee")
    void deleteTrainee_NotFound() {
        when(traineeDAO.existsById(sampleTrainee.getId())).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            traineeManager.deleteTrainee(sampleTrainee.getId());
        });

        assertEquals("Trainee with ID " + sampleTrainee.getId() + " not found for deletion.", thrown.getMessage());
        verify(traineeDAO, times(1)).existsById(sampleTrainee.getId());
        verify(traineeDAO, never()).delete(anyString());
    }

    @Test
    @DisplayName("Should return trainee when selecting by ID and found")
    void selectTraineeById_Found() {
        when(traineeDAO.findById(sampleTrainee.getId())).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> result = traineeManager.selectTraineeById(sampleTrainee.getId());

        assertTrue(result.isPresent());
        assertEquals(sampleTrainee, result.get());
        verify(traineeDAO, times(1)).findById(sampleTrainee.getId());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by ID and not found")
    void selectTraineeById_NotFound() {
        when(traineeDAO.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<Trainee> result = traineeManager.selectTraineeById("nonExistentId");

        assertTrue(result.isEmpty());
        verify(traineeDAO, times(1)).findById("nonExistentId");
    }

    @Test
    @DisplayName("Should return all trainees when selecting all")
    void selectAllTrainees_Success() {
        List<Trainee> trainees = Arrays.asList(sampleTrainee, new Trainee());
        when(traineeDAO.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeManager.selectAllTrainees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(trainees, result);
        verify(traineeDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return trainee when selecting by username and found")
    void selectTraineeByUsername_Found() {
        when(traineeDAO.findByUsername(sampleTrainee.getUsername())).thenReturn(Optional.of(sampleTrainee));

        Optional<Trainee> result = traineeManager.selectTraineeByUsername(sampleTrainee.getUsername());

        assertTrue(result.isPresent());
        assertEquals(sampleTrainee, result.get());
        verify(traineeDAO, times(1)).findByUsername(sampleTrainee.getUsername());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by username and not found")
    void selectTraineeByUsername_NotFound() {
        when(traineeDAO.findByUsername("nonExistentUsername")).thenReturn(Optional.empty());

        Optional<Trainee> result = traineeManager.selectTraineeByUsername("nonExistentUsername");

        assertTrue(result.isEmpty());
        verify(traineeDAO, times(1)).findByUsername("nonExistentUsername");
    }
}