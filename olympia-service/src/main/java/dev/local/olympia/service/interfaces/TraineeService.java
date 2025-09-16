package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.trainee.requests.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.requests.TraineeUpdateRequest;
import dev.local.olympia.dto.trainee.responses.TraineeProfileResponse;
import dev.local.olympia.dto.trainer.responses.TrainerResponse;
import dev.local.olympia.dto.training.responses.TrainingResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    AuthCredentials createTrainee(TraineeCreationRequest request);

    Optional<Trainee> selectTraineeById(String id);
    Optional<TraineeProfileResponse> selectTraineeByUsername(String username);
    List<Trainee> selectAllTrainees();

    TraineeProfileResponse updateTrainee(TraineeUpdateRequest request);
    Trainee updateTraineePassword(String id, String newPassword);
    Trainee activateDeactivateTrainee(String username, boolean isActive);
    List<TrainerResponse> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames);

    boolean authenticateTrainee(String username, String password);

    void deleteTrainee(String username);

    List<TrainingResponse> getTraineeTrainingsList(String username, LocalDate fromDate, LocalDate toDate,
                                                   String trainerName, String trainingType);
}
