package dev.local.olympia.domain;

import java.time.LocalDate;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;

    public Trainee(){
        super();
    }

    public Trainee(String firstName, String lastName, String username, String password, LocalDate dateOfBirth, String address) {
        super(firstName, lastName, username, password);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    // --- Getters ---
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public String getAddress() {
        return address;
    }

    // --- Setters ---
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id='" + getId() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", isActive=" + isActive() +
                '}';
    }
}
