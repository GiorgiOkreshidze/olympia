package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.TrainerUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Trainer createTrainer(TrainerCreationRequest request);
    Trainer updateTrainer(TrainerUpdateRequest request);
    Optional<Trainer> selectTrainerById(String id);
    List<Trainer> selectAllTrainers();
    Optional<Trainer> selectTrainerByUsername(String username);
}
