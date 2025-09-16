package dev.local.olympia.dto.trainee.responses;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class TraineeProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
    private Set<TrainerResponse> trainers;

    public static class TrainerResponse {
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

    public TraineeProfileResponse() {
    }

    public TraineeProfileResponse(Trainee trainee){
        this.username = trainee.getUser().getUsername();
        this.firstName = trainee.getUser().getFirstName();
        this.lastName = trainee.getUser().getLastName();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
        this.isActive = trainee.getUser().isActive();
        this.trainers = trainee.getTrainers().stream().map(TrainerResponse::new).collect(Collectors.toSet());
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
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public String getAddress() {
        return address;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public Set<TrainerResponse> getTrainers() {
        return trainers;
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
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public void setTrainers(Set<TrainerResponse> trainers) {
        this.trainers = trainers;
    }
}
