package dev.local.olympia.dto.training;

import dev.local.olympia.domain.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public class TrainingCreationRequest {
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Duration trainingDuration;

    public TrainingCreationRequest() {
    }

    public TrainingCreationRequest(String traineeId, String trainerId, String trainingName,
                                   TrainingType trainingType, LocalDate trainingDate, Duration trainingDuration) {
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    // Getters
    public String getTraineeId() {
        return traineeId;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public Duration getTrainingDuration() {
        return trainingDuration;
    }

    // Setters
    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setTrainingDuration(Duration trainingDuration) {
        this.trainingDuration = trainingDuration;
    }
}
