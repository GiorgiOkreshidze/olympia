package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(TraineeCreationRequest request);

    Optional<Trainee> selectTraineeById(String id);
    Optional<Trainee> selectTraineeByUsername(String username);
    List<Trainee> selectAllTrainees();

    Trainee updateTrainee(TraineeUpdateRequest request);
    Trainee updateTraineePassword(String id, String newPassword);
    Trainee activateDeactivateTrainee(String username, boolean isActive);

    boolean authenticateTrainee(String username, String password);

    void deleteTrainee(String username);

    List<Training> getTrainingsByTrainee(String username, LocalDate fromDate, LocalDate toDate,
                                                String trainerName, String trainingType);
}
