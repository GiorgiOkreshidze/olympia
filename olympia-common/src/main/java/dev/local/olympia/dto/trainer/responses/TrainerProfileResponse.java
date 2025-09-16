package dev.local.olympia.dto.trainer.responses;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;

import java.util.Set;
import java.util.stream.Collectors;

public class TrainerProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private Set<TraineeResponse> trainees;

    public static class TraineeResponse{
        private String username;
        private String firstName;
        private String lastName;

        public TraineeResponse() { }

        public TraineeResponse(Trainee trainee){
            this.username = trainee.getUser().getUsername();
            this.firstName = trainee.getUser().getFirstName();
            this.lastName = trainee.getUser().getLastName();
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
    }

    public TrainerProfileResponse() { }

    public TrainerProfileResponse(Trainer trainer){
        this.username = trainer.getUser().getUsername();
        this.firstName = trainer.getUser().getFirstName();
        this.lastName = trainer.getUser().getLastName();
        this.specialization = trainer.getSpecialization().getTrainingTypeName(); // Assuming specialization has a name field
        this.isActive = trainer.getUser().isActive();
        this.trainees = trainer.getTrainees().stream()
                .map(TraineeResponse::new)
                .collect(Collectors.toSet());
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
    public boolean isActive() {
        return isActive;
    }
    public Set<TraineeResponse> getTrainees() {
        return trainees;
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
    public void setActive(boolean active) {
        isActive = active;
    }
    public void setTrainees(Set<TraineeResponse> trainees) {
        this.trainees = trainees;
    }
}
