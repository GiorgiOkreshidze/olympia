package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.dto.training.TrainingCreationRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingManagerTests {

    @Mock
    private TrainingDAO trainingDAO;
    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainingManager trainingManager;

    private TrainingCreationRequest creationRequest;
    private Training sampleTraining;
    private String validTraineeId;
    private String validTrainerId;

    @BeforeEach
    void setUp() {
        validTraineeId = UUID.randomUUID().toString();
        validTrainerId = UUID.randomUUID().toString();

        creationRequest = new TrainingCreationRequest(
                validTraineeId,
                validTrainerId,
                "Morning Cardio",
                TrainingType.SPEED,
                LocalDate.of(2025, 8, 1),
                Duration.ofMinutes(60)
        );
        sampleTraining = new Training( validTraineeId, validTrainerId, "Evening Yoga",
                TrainingType.FLEXIBILITY, LocalDate.of(2025, 8, 2), Duration.ofHours(1)
        );
    }

    @Test
    @DisplayName("Should successfully create a new training session")
    void createTraining_Success() {
        when(traineeDAO.existsById(validTraineeId)).thenReturn(true);
        when(trainerDAO.existsById(validTrainerId)).thenReturn(true);
        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> {
            return invocation.<Training>getArgument(0);
        });

        Training createdTraining = trainingManager.createTraining(creationRequest);

        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getId());
        assertEquals("Morning Cardio", createdTraining.getTrainingName());
        assertEquals(TrainingType.SPEED, createdTraining.getTrainingType());
        assertEquals(validTraineeId, createdTraining.getTraineeId());
        assertEquals(validTrainerId, createdTraining.getTrainerId());

        verify(traineeDAO, times(1)).existsById(validTraineeId);
        verify(trainerDAO, times(1)).existsById(validTrainerId);
        verify(trainingDAO, times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if trainee not found during training creation")
    void createTraining_TraineeNotFound() {
        when(traineeDAO.existsById(validTraineeId)).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainingManager.createTraining(creationRequest);
        });

        assertEquals("Trainee with ID " + validTraineeId + " not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).existsById(validTraineeId);
        verify(trainerDAO, never()).existsById(anyString());
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if trainer not found during training creation")
    void createTraining_TrainerNotFound() {
        when(traineeDAO.existsById(validTraineeId)).thenReturn(true);
        when(trainerDAO.existsById(validTrainerId)).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainingManager.createTraining(creationRequest);
        });

        assertEquals("Trainer with ID " + validTrainerId + " not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).existsById(validTraineeId);
        verify(trainerDAO, times(1)).existsById(validTrainerId);
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    @DisplayName("Should return training when selecting by ID and found")
    void selectTrainingById_Found() {
        when(trainingDAO.findById(sampleTraining.getId())).thenReturn(Optional.of(sampleTraining));

        Optional<Training> result = trainingManager.selectTrainingById(sampleTraining.getId());

        assertTrue(result.isPresent());
        assertEquals(sampleTraining, result.get());
        verify(trainingDAO, times(1)).findById(sampleTraining.getId());
    }

    @Test
    @DisplayName("Should return empty optional when selecting by ID and not found")
    void selectTrainingById_NotFound() {
        when(trainingDAO.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<Training> result = trainingManager.selectTrainingById("nonExistentId");

        assertTrue(result.isEmpty());
        verify(trainingDAO, times(1)).findById("nonExistentId");
    }

    @Test
    @DisplayName("Should return all trainings when selecting all")
    void selectAllTrainings_Success() {
        List<Training> trainings = Arrays.asList(sampleTraining, new Training());
        when(trainingDAO.findAll()).thenReturn(trainings);

        List<Training> result = trainingManager.selectAllTrainings();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(trainings, result);
        verify(trainingDAO, times(1)).findAll();
    }
}