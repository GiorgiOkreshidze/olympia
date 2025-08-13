package dev.local.olympia.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_trainer_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "specialization_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_trainer_specialization")
    )
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees = new HashSet<>();

    public Trainer(){
    }

    public Trainer(String firstName, String lastName, String username, String password, TrainingType specialization) {
        this.user = new User(firstName, lastName, username, password);
        this.specialization = specialization;
    }

    // --- Getters ---
    public TrainingType getSpecialization() {
        return specialization;
    }
    public User getUser() {
        return user;
    }
    public Set<Trainee> getTrainees() {
        return trainees;
    }

    // --- Setters ---
    public void setSpecialization(TrainingType specialization) {
        this.specialization = specialization;
    }
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.id = user.getId();
        }
    }
    public void setTrainees(Set<Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;
        return user != null && user.equals(trainer.user);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "id='" + id + '\'' +
                ", firstName='" + (user != null ? user.getFirstName() : null) + '\'' +
                ", lastName='" + (user != null ? user.getLastName() : null) + '\'' +
                ", username='" + (user != null ? user.getUsername() : null) + '\'' +
                ", specialization='" + specialization + '\'' +
                ", isActive=" + (user != null && user.isActive()) +
                '}';
    }
}
