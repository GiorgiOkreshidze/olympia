package dev.local.olympia.dto.trainee.requests;

import java.time.LocalDate;

public class TraineeUpdateRequest {

    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;

    public TraineeUpdateRequest() {
    }

    public TraineeUpdateRequest(
            String username,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String address,
            Boolean isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.isActive = isActive;
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

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
