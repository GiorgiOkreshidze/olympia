package dev.local.olympia.impl;

import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.interfaces.MapStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainingDAOTests {

    @Mock
    private MapStorage<Object> mapStorage;

    @InjectMocks
    private InMemoryTrainingDAO trainingDAO;

    private Training training1;
    private Training training2;
    private Map<String, Training> trainingMap;

    @BeforeEach
    void setUp() {
        training1 = new Training("traineeId1", "trainerId1", "Morning Run", TrainingType.SPEED, LocalDate.of(2025, 1, 1), Duration.ofHours(1));
        training2 = new Training("traineeId2", "trainerId2", "Evening Yoga", TrainingType.FLEXIBILITY, LocalDate.of(2025, 1, 2), Duration.ofMinutes(45));

        trainingMap = new ConcurrentHashMap<>();
        trainingMap.put(training1.getId(), training1);
        trainingMap.put(training2.getId(), training2);

        when(mapStorage.getStorage(Training.class)).thenReturn((Map<String, Training>) (Map<?, ?>) trainingMap);
    }

    @Test
    @DisplayName("Should find all trainings when the map is not empty")
    void findAll_NotEmpty() {
        List<Training> allTrainings = trainingDAO.findAll();

        assertNotNull(allTrainings);
        assertEquals(2, allTrainings.size());
        assertTrue(allTrainings.contains(training1));
        assertTrue(allTrainings.contains(training2));
        verify(mapStorage, times(1)).getStorage(Training.class);
    }

    @Test
    @DisplayName("Should return empty list when the map is empty")
    void findAll_Empty() {
        trainingMap.clear();

        List<Training> allTrainings = trainingDAO.findAll();

        assertNotNull(allTrainings);
        assertTrue(allTrainings.isEmpty());
        verify(mapStorage, times(1)).getStorage(Training.class);
    }
}