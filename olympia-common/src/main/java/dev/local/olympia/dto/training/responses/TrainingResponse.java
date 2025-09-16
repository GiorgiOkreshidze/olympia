package dev.local.olympia.dto.training.responses;

import dev.local.olympia.domain.Training;

import java.time.LocalDate;

public class TrainingResponse {
    private String trainingName;
    private LocalDate trainingDate;
    private String trainingType;
    private Long trainingDuration;
    private String trainerName;

    public TrainingResponse() { }

    public TrainingResponse(Training training) {
        this.trainingName = training.getTrainingName();
        this.trainingDate = training.getTrainingDate();
        this.trainingType = training.getTrainingType().getTrainingTypeName();
        this.trainingDuration = training.getTrainingDuration() != null ? training.getTrainingDuration().toMinutes() : null;
        this.trainerName = training.getTrainer() != null ? training.getTrainer().getUser().getFirstName() : null;
    }

    // Getters
    public String getTrainingName() {
        return trainingName;
    }
    public LocalDate getTrainingDate() {
        return trainingDate;
    }
    public String getTrainingType() {
        return trainingType;
    }
    public Long getTrainingDuration() {
        return trainingDuration;
    }
    public String getTrainerName() {
        return trainerName;
    }

    // Setters
    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }
    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }
    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }
    public void setTrainingDuration(Long trainingDuration) {
        this.trainingDuration = trainingDuration;
    }
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
