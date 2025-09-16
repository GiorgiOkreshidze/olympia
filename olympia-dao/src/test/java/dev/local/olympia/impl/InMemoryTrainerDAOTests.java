package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.interfaces.MapStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainerDAOTests {

    /*@Mock
    private MapStorage<Object> mapStorage;

    @InjectMocks
    private InMemoryTrainerDAO trainerDAO;

    private Trainer trainer1;
    private Trainer trainer2;
    private Map<String, Trainer> trainerMap;

    @BeforeEach
    void setUp() {
        trainer1 = new Trainer("Alice", "Johnson", "Alice.Johnson", "randomPass", "Yoga");
        trainer2 = new Trainer("Bob", "Williams","bob.williams", "passB", "Pilates");

        trainerMap = new ConcurrentHashMap<>();
        trainerMap.put(trainer1.getId(), trainer1);
        trainerMap.put(trainer2.getId(), trainer2);

        when(mapStorage.getStorage(Trainer.class)).thenReturn((Map<String, Trainer>) (Map<?, ?>) trainerMap);
    }

    @Test
    @DisplayName("Should find all trainers when the map is not empty")
    void findAll_NotEmpty() {
        List<Trainer> allTrainers = trainerDAO.findAll();

        assertNotNull(allTrainers);
        assertEquals(2, allTrainers.size());
        assertTrue(allTrainers.contains(trainer1));
        assertTrue(allTrainers.contains(trainer2));
        verify(mapStorage, times(1)).getStorage(Trainer.class);
    }

    @Test
    @DisplayName("Should return empty list when the map is empty")
    void findAll_Empty() {
        trainerMap.clear();

        List<Trainer> allTrainers = trainerDAO.findAll();

        assertNotNull(allTrainers);
        assertTrue(allTrainers.isEmpty());
        verify(mapStorage, times(1)).getStorage(Trainer.class);
    }

    @Test
    @DisplayName("Should find a trainer by username when exists")
    void findByUsername_Exists() {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername(trainer1.getUsername());

        assertTrue(foundTrainer.isPresent());
        assertEquals(trainer1, foundTrainer.get());
        verify(mapStorage, times(1)).getStorage(Trainer.class);
    }

    @Test
    @DisplayName("Should return empty optional when trainer not found by username")
    void findByUsername_NotExists() {
        Optional<Trainer> foundTrainer = trainerDAO.findByUsername("nonExistentUsername");

        assertFalse(foundTrainer.isPresent());
        verify(mapStorage, times(1)).getStorage(Trainer.class);
    }

    @Test
    @DisplayName("Should find trainers by first and last name when exist")
    void findByFirstNameAndLastName_Exists() {
        Trainer trainer3 = new Trainer("Alice", "Johnson", "Alice.Johnson", "passC", "Strength");
        trainerMap.put(trainer3.getId(), trainer3);

        List<Trainer> result = trainerDAO.findByFirstNameAndLastName("Alice", "Johnson");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(trainer1));
        assertTrue(result.contains(trainer3));
        verify(mapStorage, times(1)).getStorage(Trainer.class);
    }

    @Test
    @DisplayName("Should return empty list when no trainers found by first and last name")
    void findByFirstNameAndLastName_NotExists() {
        List<Trainer> result = trainerDAO.findByFirstNameAndLastName("Non", "Existent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mapStorage, times(1)).getStorage(Trainer.class);
    }*/
}