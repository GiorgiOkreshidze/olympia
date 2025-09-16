package dev.local.olympia.dto.training.responses;

public class TrainingTypeResponse {
    private String TrainingType;
    private String TrainingTypeId;

    public TrainingTypeResponse() {}

    public TrainingTypeResponse(String trainingType, String trainingTypeId) {
        TrainingType = trainingType;
        TrainingTypeId = trainingTypeId;
    }

    // Getters
    public String getTrainingType() {
        return TrainingType;
    }
    public String getTrainingTypeId() {
        return TrainingTypeId;
    }

    // Setters
    public void setTrainingType(String trainingType) {
        TrainingType = trainingType;
    }
    public void setTrainingTypeId(String trainingTypeId) {
        TrainingTypeId = trainingTypeId;
    }
}
