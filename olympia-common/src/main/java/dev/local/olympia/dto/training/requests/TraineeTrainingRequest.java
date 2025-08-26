package dev.local.olympia.dto.training.requests;

import java.time.LocalDate;

public class TraineeTrainingRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private String trainingType;

    public TraineeTrainingRequest() { }

    public TraineeTrainingRequest(LocalDate periodFrom, LocalDate periodTo,
                                  String trainerName, String trainingType) {
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.trainerName = trainerName;
        this.trainingType = trainingType;
    }

    // Getters
    public LocalDate getPeriodFrom() {
        return periodFrom;
    }
    public LocalDate getPeriodTo() {
        return periodTo;
    }
    public String getTrainerName() {
        return trainerName;
    }
    public String getTrainingType() {
        return trainingType;
    }

    // Setters
    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }
    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }
    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }
}
