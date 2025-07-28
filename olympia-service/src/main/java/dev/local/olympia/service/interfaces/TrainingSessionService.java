package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.training.TrainingCreationRequest;

import java.util.List;
import java.util.Optional;

public interface TrainingSessionService {
    Training createTraining(TrainingCreationRequest request);
    Optional<Training> selectTrainingById(String id);
    List<Training> selectAllTrainings();
}
