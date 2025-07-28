package dev.local.olympia.dto.trainer;

public class TrainerUpdateRequest {
    private String id; // ID is crucial for updates
    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive; // Use Boolean to allow null (no change) or true/false

    public TrainerUpdateRequest() {
    }

    public TrainerUpdateRequest(String id, String firstName, String lastName,  String specialization, Boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.isActive = isActive;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
