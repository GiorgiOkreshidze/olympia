package dev.local.olympia.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Training {
    private String id;
    private String traineeId;
    private String trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Duration trainingDuration;

    public Training(){
        this.id = UUID.randomUUID().toString();
    }

    public Training(String traineeId, String trainerId, String trainingName, TrainingType trainingType, LocalDate trainingDate, Duration trainingDuration) {
        this();
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }
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

    // --- Setters ---
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id.equals(training.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", traineeId='" + traineeId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}
