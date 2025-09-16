package dev.local.olympia.dto.trainer.responses;

import dev.local.olympia.domain.Trainer;

public class TrainerResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;

    public TrainerResponse() {
    }

    public TrainerResponse(Trainer trainer) {
        this.username = trainer.getUser().getUsername();
        this.firstName = trainer.getUser().getFirstName();
        this.lastName = trainer.getUser().getLastName();
        this.specialization = trainer.getSpecialization().getTrainingTypeName();
    }

    // Getters
    public String getUsername() {
        return username;
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

    // Setters
    public void setUsername(String username) {
        this.username = username;
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
}
