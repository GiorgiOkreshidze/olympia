package dev.local.olympia.interfaces;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingDAO {
    Training save(Training training);
    Optional<Training> findById(String id);
    List<Training> findAll();

    List<Training> findTrainingsByTrainee(Trainee trainee, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType);
}
