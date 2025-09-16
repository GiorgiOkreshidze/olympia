package dev.local.olympia.dto.training.requests;

import java.time.LocalDate;

public class TrainerTrainingRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String traineeName;

    public TrainerTrainingRequest() { }

    public TrainerTrainingRequest(LocalDate periodFrom, LocalDate periodTo,
                                  String traineeName) {
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.traineeName = traineeName;
    }

    // Getters
    public LocalDate getPeriodFrom() {
        return periodFrom;
    }
    public LocalDate getPeriodTo() {
        return periodTo;
    }
    public String getTraineeName() {
        return traineeName;
    }

    // Setters
    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }
    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }
    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }
}
