package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.dto.training.TrainingCreationRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import dev.local.olympia.interfaces.TrainingTypeDAO;
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
    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingManager trainingManager;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;
    private TrainingCreationRequest creationRequest;
    private Training sampleTraining;

    @BeforeEach
    void setUp() {
        trainee = new Trainee(
                "firstNameTrainee",
                "lastNameTrainee",
                "firstNameTrainee.lastNameTrainee",
                "passwordTrainee",
                LocalDate.of(1990, 1, 1),
                "addressTrainee"
        );
        trainee.getUser().setId(UUID.randomUUID().toString());

        trainingType = new TrainingType("Yoga");

        trainer = new Trainer(
                "firstNameTrainer",
                "lastNameTrainer",
                "firstNameTrainer.lastNameTrainer",
                "passwordTrainer",
                trainingType
        );

        creationRequest = new TrainingCreationRequest(
                trainee.getUser().getId(),
                trainer.getUser().getId(),
                "Morning Cardio",
                trainingType.getTrainingTypeName(),
                LocalDate.of(2025, 8, 1),
                Duration.ofMinutes(60)
        );

        sampleTraining = new Training(
                trainee,
                trainer,
                "Sample Training",
                trainingType,
                LocalDate.of(2025, 8, 1),
                Duration.ofMinutes(60)
        );
    }

    @Test
    @DisplayName("Should successfully create a new training session")
    void createTraining_Success() {
        when(traineeDAO.existsById(trainee.getUser().getId())).thenReturn(true);
        when(trainerDAO.existsById(trainer.getUser().getId())).thenReturn(true);
        when(trainingDAO.save(any(Training.class))).thenAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(UUID.randomUUID().toString()); // Simulate ID generation
            return training;
        });
        when(traineeDAO.findById(trainee.getUser().getId())).thenReturn(Optional.of(trainee));
        when(trainerDAO.findById(trainer.getUser().getId())).thenReturn(Optional.of(trainer));
        when(trainingTypeDAO.findByName(trainingType.getTrainingTypeName())).thenReturn(trainingType);

        Training createdTraining = trainingManager.createTraining(creationRequest);

        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getId());
        assertEquals("Morning Cardio", createdTraining.getTrainingName());
        assertEquals(trainingType, createdTraining.getTrainingType());
        assertEquals(trainee, createdTraining.getTrainee());
        assertEquals(trainer, createdTraining.getTrainer());

        verify(traineeDAO, times(1)).existsById(trainee.getUser().getId());
        verify(trainerDAO, times(1)).existsById(trainer.getUser().getId());
        verify(trainingDAO, times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if trainee not found during training creation")
    void createTraining_TraineeNotFound() {
        when(traineeDAO.existsById(trainee.getUser().getId())).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainingManager.createTraining(creationRequest);
        });

        assertEquals("Trainee with ID " + trainee.getUser().getId() + " not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).existsById(trainee.getUser().getId());
        verify(trainerDAO, never()).existsById(anyString());
        verify(trainingDAO, never()).save(any(Training.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException if trainer not found during training creation")
    void createTraining_TrainerNotFound() {
        when(traineeDAO.existsById(trainee.getUser().getId())).thenReturn(true);
        when(trainerDAO.existsById(trainer.getUser().getId())).thenReturn(false);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            trainingManager.createTraining(creationRequest);
        });

        assertEquals("Trainer with ID " + trainer.getUser().getId() + " not found.", thrown.getMessage());
        verify(traineeDAO, times(1)).existsById(trainee.getUser().getId());
        verify(trainerDAO, times(1)).existsById(trainer.getUser().getId());
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

    /*


    */
}