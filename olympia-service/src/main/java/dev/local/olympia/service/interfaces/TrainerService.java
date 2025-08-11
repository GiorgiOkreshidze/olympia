package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(TrainerCreationRequest request);

    Optional<Trainer> selectTrainerById(String id);
    Optional<Trainer> selectTrainerByUsername(String username);
    List<Trainer> selectAllTrainers();

    Trainer updateTrainer(TrainerUpdateRequest request);
    Trainer updateTrainerPassword(String id, String newPassword);
    Trainer activateDeactivateTrainer(String username, boolean isActive);

    List<Training> getTrainerTrainingsList(String username, LocalDate fromDate, LocalDate toDate, String traineeName);
}
