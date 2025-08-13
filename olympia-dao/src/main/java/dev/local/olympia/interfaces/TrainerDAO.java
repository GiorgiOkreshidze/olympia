package dev.local.olympia.interfaces;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    Trainer save(Trainer trainer);

    Optional<Trainer> findById(String id);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> findAll();
    boolean existsById(String id);
    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName);
    List<Training> findTrainingsByTrainer(Trainer trainer, LocalDate fromDate, LocalDate toDate, String traineeName);
    List<Trainer> findUnassignedTrainers(String traineeUsername);
}
