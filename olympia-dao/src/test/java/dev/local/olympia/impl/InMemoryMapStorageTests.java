package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMapStorageTests {

    private InMemoryMapStorage mapStorage;

    @BeforeEach
    void setUp() {
        mapStorage = new InMemoryMapStorage();
    }

    @Test
    @DisplayName("Should return an empty map for a new entity type")
    void getStorage_NewEntityType_ReturnsEmptyMap() {
        Map<String, Trainee> traineeMap = mapStorage.getStorage(Trainee.class);
        assertNotNull(traineeMap);
        assertTrue(traineeMap.isEmpty());
    }

    @Test
    @DisplayName("Should return the same map for the same entity type on subsequent calls")
    void getStorage_ExistingEntityType_ReturnsSameMap() {
        Map<String, Trainee> map1 = mapStorage.getStorage(Trainee.class);
        Map<String, Trainee> map2 = mapStorage.getStorage(Trainee.class);
        assertSame(map1, map2);
    }

    @Test
    @DisplayName("Should save a new entity and retrieve it successfully")
    void save_NewEntity_Success() {
        Trainee trainee = new Trainee("Test", "User", "Test.User", "randomPass", LocalDate.now(), "123 Test St");
        mapStorage.save(Trainee.class, trainee.getId(), trainee);

        Map<String, Trainee> retrievedMap = mapStorage.getStorage(Trainee.class);
        assertTrue(retrievedMap.containsKey(trainee.getId()));
        assertEquals(trainee, retrievedMap.get(trainee.getId()));
    }

    @Test
    @DisplayName("Should correctly handle multiple entity types")
    void handleMultipleEntityTypes() {
        Trainee trainee = new Trainee("Trainee", "One", "Trainee.One", "randomPass", LocalDate.now(), "Address T");
        Trainer trainer = new Trainer("Trainer", "Alpha", "Trainer.Alpha", "randomPass", "Strength");
        Training training = new Training(trainee.getId(), trainer.getId(), "CrossFit", TrainingType.STRENGTH, LocalDate.now(), Duration.ofHours(1));

        mapStorage.save(Trainee.class, trainee.getId(), trainee);
        mapStorage.save(Trainer.class, trainer.getId(), trainer);
        mapStorage.save(Training.class, training.getId(), training);

        assertEquals(1, mapStorage.getStorage(Trainee.class).size());
        assertEquals(1, mapStorage.getStorage(Trainer.class).size());
        assertEquals(1, mapStorage.getStorage(Training.class).size());

        assertTrue(mapStorage.getStorage(Trainee.class).containsKey(trainee.getId()));
        assertTrue(mapStorage.getStorage(Trainer.class).containsKey(trainer.getId()));
        assertTrue(mapStorage.getStorage(Training.class).containsKey(training.getId()));
    }
}
