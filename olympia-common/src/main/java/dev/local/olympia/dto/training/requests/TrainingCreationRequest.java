package dev.local.olympia.dto.training.requests;

import java.time.Duration;
import java.time.LocalDate;

public class TrainingCreationRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private Duration trainingDuration;

    public TrainingCreationRequest() {
    }

    public TrainingCreationRequest(String traineeUsername, String trainerUsername, String trainingName,
                                   String trainingType, LocalDate trainingDate, Duration trainingDuration) {
        this.traineeUsername = traineeUsername;
        this.trainerUsername = trainerUsername;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    // Getters
    public String getTraineeUsername() {
        return traineeUsername;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public Duration getTrainingDuration() {
        return trainingDuration;
    }

    // Setters
    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setTrainingDuration(Duration trainingDuration) {
        this.trainingDuration = trainingDuration;
    }
}
