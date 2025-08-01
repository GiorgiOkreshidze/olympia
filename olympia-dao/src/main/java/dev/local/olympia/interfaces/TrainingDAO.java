package dev.local.olympia.interfaces;

import dev.local.olympia.domain.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDAO {
    Training save(Training training);
    Optional<Training> findById(String id);
    List<Training> findAll();
    void delete(String id); // Optional: if trainings can be deleted
    boolean existsById(String id);
}
