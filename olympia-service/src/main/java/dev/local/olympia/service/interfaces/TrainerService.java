package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.trainer.requests.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.requests.TrainerUpdateRequest;
import dev.local.olympia.dto.trainer.responses.TrainerProfileResponse;
import dev.local.olympia.dto.trainer.responses.TrainerResponse;
import dev.local.olympia.dto.training.responses.TrainingResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    AuthCredentials createTrainer(TrainerCreationRequest request);

    Optional<Trainer> selectTrainerById(String id);
    Optional<TrainerProfileResponse> selectTrainerByUsername(String username);
    List<Trainer> selectAllTrainers();
    List<TrainingResponse> getTrainerTrainingsList(String username, LocalDate fromDate, LocalDate toDate, String traineeName);
    List<TrainerResponse> findUnassignedTrainers(String traineeUsername);

    TrainerProfileResponse updateTrainer(TrainerUpdateRequest request);
    Trainer updateTrainerPassword(String id, String newPassword);
    Trainer activateDeactivateTrainer(String username, boolean isActive);

    boolean authenticateTrainer(String username, String password);
}
