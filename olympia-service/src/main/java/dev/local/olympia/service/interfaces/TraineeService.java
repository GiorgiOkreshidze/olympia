package dev.local.olympia.service.interfaces;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.AuthCredentials;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    Trainee createTrainee(TraineeCreationRequest request);

    Optional<Trainee> selectTraineeById(String id, AuthCredentials credentials);
    Optional<Trainee> selectTraineeByUsername(String username, AuthCredentials credentials);
    List<Trainee> selectAllTrainees(AuthCredentials credentials);

    Trainee updateTrainee(TraineeUpdateRequest request, AuthCredentials credentials);
    Trainee updateTraineePassword(String id, String newPassword, AuthCredentials credentials);
    Trainee activateDeactivateTrainee(String username, boolean isActive, AuthCredentials credentials);
    void updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames, AuthCredentials credentials);

    boolean authenticateTrainee(String username, String password);

    void deleteTrainee(String username, AuthCredentials credentials);

    List<Training> getTraineeTrainingsList(String username, LocalDate fromDate, LocalDate toDate,
                                                String trainerName, String trainingType, AuthCredentials credentials);
}
