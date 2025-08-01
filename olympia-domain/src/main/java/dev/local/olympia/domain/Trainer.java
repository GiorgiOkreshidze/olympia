package dev.local.olympia.domain;

public class Trainer extends User {
    private String specialization;

    public Trainer(){
        super();
    }

    public Trainer(String firstName, String lastName, String username, String password, String specialization) {
        super(firstName, lastName, username, password);
        this.specialization = specialization;
    }

    // --- Getters ---
    public String getSpecialization() {
        return specialization;
    }

    // --- Setters ---
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
        return "Trainer{" +
                "id='" + getId() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", isActive=" + isActive() +
                '}';
    }
}
