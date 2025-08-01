package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(TraineeCreationRequest request);
    Trainee updateTrainee(TraineeUpdateRequest request);
    void deleteTrainee(String id);
    Optional<Trainee> selectTraineeById(String id);
    List<Trainee> selectAllTrainees();
    Optional<Trainee> selectTraineeByUsername(String username);
}
