package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.interfaces.MapStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InMemoryTraineeDAOTests {

    /*@Mock
    private MapStorage<Object> mapStorage;

    @InjectMocks
    private InMemoryTraineeDAO traineeDAO;

    private Trainee trainee1;
    private Trainee trainee2;
    private Map<String, Trainee> traineeMap;

    @BeforeEach
    void setUp() {
        trainee1 = new Trainee("John", "Doe", "John.Doe", "randomPass", LocalDate.of(1990, 1, 1), "123 Main St");
        trainee2 = new Trainee("Jane", "Smith", "Jane.Smith", "randomPass", LocalDate.of(1992, 2, 2), "456 Oak Ave");

        traineeMap = new ConcurrentHashMap<>();
        traineeMap.put(trainee1.getId(), trainee1);
        traineeMap.put(trainee2.getId(), trainee2);
    }

    @Test
    @DisplayName("Should save a new trainee and return the saved trainee")
    void save_NewTrainee_Success() {
        Trainee newTrainee = new Trainee("Alice", "Wonder", "Alice.Wonder", "randomPass", LocalDate.of(1995, 3, 3), "789 Pine Ln");
        String originalId = newTrainee.getId();

        when(mapStorage.save(eq(Trainee.class), eq(newTrainee.getId()), any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(2));

        Trainee savedTrainee = traineeDAO.save(newTrainee);

        assertNotNull(savedTrainee);
        assertNotNull(savedTrainee.getId());
        assertEquals(originalId, savedTrainee.getId());
        assertEquals(newTrainee.getFirstName(), savedTrainee.getFirstName());

        verify(mapStorage, times(1)).save(Trainee.class, savedTrainee.getId(), savedTrainee);
    }

    @Test
    @DisplayName("Should save an existing trainee (update) and return the updated trainee")
    void save_ExistingTrainee_Success() {
        Trainee existingTrainee = new Trainee("John", "Updated", "John.Updated", "randomPass", LocalDate.of(1990, 1, 1), "New Address");

        when(mapStorage.save(eq(Trainee.class), eq(existingTrainee.getId()), any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(2));

        Trainee updatedTrainee = traineeDAO.save(existingTrainee);

        assertNotNull(updatedTrainee.getId());
        assertEquals(existingTrainee.getId(), updatedTrainee.getId());
        assertEquals("Updated", updatedTrainee.getLastName());
        assertEquals("New Address", updatedTrainee.getAddress());

        verify(mapStorage, times(1)).save(Trainee.class, updatedTrainee.getId(), updatedTrainee);
    }

    @Test
    @DisplayName("Should find a trainee by ID when exists")
    void findById_Exists() {
        when(mapStorage.findById(eq(Trainee.class), eq(trainee1.getId())))
                .thenReturn(Optional.of(trainee1));

        Optional<Trainee> foundTrainee = traineeDAO.findById(trainee1.getId());

        assertTrue(foundTrainee.isPresent());
        assertEquals(trainee1, foundTrainee.get());
    }

    @Test
    @DisplayName("Should return empty optional when trainee not found by ID")
    void findById_NotExists() {
        when(mapStorage.findById(eq(Trainee.class), eq("nonExistentId")))
                .thenReturn(Optional.empty());

        Optional<Trainee> foundTrainee = traineeDAO.findById("nonExistentId");

        assertFalse(foundTrainee.isPresent());
    }

    @Test
    @DisplayName("Should return true when trainee exists by ID")
    void existsById_True() {
        when(mapStorage.existsById(eq(Trainee.class), eq(trainee1.getId())))
                .thenReturn(true);

        assertTrue(traineeDAO.existsById(trainee1.getId()));
    }

    @Test
    @DisplayName("Should return false when trainee does not exist by ID")
    void existsById_False() {
        when(mapStorage.existsById(eq(Trainee.class), eq("nonExistentId")))
                .thenReturn(false);
        assertFalse(traineeDAO.existsById("nonExistentId"));
    }

    @Test
    @DisplayName("Should return empty list when the map is empty")
    void findAll_Empty() {
        traineeMap.clear();

        List<Trainee> allTrainees = traineeDAO.findAll();

        assertNotNull(allTrainees);
        assertTrue(allTrainees.isEmpty());
        verify(mapStorage, times(1)).getStorage(Trainee.class);
    }

    @Test
    @DisplayName("Should return empty optional when trainee not found by username")
    void findByUsername_NotExists() {
        Optional<Trainee> foundTrainee = traineeDAO.findByUsername("nonExistentUsername");

        assertFalse(foundTrainee.isPresent());
        verify(mapStorage, times(1)).getStorage(Trainee.class);
    }

    @Test
    @DisplayName("Should return empty list when no trainees found by first and last name")
    void findByFirstNameAndLastName_NotExists() {
        List<Trainee> result = traineeDAO.findByFirstNameAndLastName("Non", "Existent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mapStorage, times(1)).getStorage(Trainee.class);
    }*/
}