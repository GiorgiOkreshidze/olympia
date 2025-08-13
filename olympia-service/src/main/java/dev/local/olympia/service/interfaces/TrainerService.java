package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.AuthCredentials;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(TrainerCreationRequest request);

    Optional<Trainer> selectTrainerById(String id, AuthCredentials credentials);
    Optional<Trainer> selectTrainerByUsername(String username, AuthCredentials credentials);
    List<Trainer> selectAllTrainers(AuthCredentials credentials);
    List<Training> getTrainerTrainingsList(String username, LocalDate fromDate, LocalDate toDate, String traineeName, AuthCredentials credentials);
    List<Trainer> findUnassignedTrainers(String traineeUsername, AuthCredentials credentials);

    Trainer updateTrainer(TrainerUpdateRequest request, AuthCredentials credentials);
    Trainer updateTrainerPassword(String id, String newPassword, AuthCredentials credentials);
    Trainer activateDeactivateTrainer(String username, boolean isActive, AuthCredentials credentials);

    boolean authenticateTrainer(String username, String password);
}
