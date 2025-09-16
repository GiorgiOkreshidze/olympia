package dev.local.olympia.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_trainee_user")
    )
    private User user;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(
                    name = "trainee_id",
                    foreignKey = @ForeignKey(name = "fk_trainee_trainer_trainee")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "trainer_id",
                    foreignKey = @ForeignKey(name = "fk_trainee_trainer_trainer")
            )
    )
    private Set<Trainer> trainers = new HashSet<>();

    public Trainee(){
    }

    public Trainee(String firstName, String lastName, String username, String password, LocalDate dateOfBirth, String address) {
        this.user = new User(firstName, lastName, username, password);
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
    public Set<Trainer> getTrainers() {
        return trainers;
    }
    public User getUser() {
        return user;
    }


    // --- Setters ---
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setTrainers(Set<Trainer> trainers) {
        this.trainers = trainers;
    }
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.id = user.getId();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainee trainee)) return false;
        return user != null && user.equals(trainee.user);
    }

    @Override
    public int hashCode() {
        return user != null ? user.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id='" + id + '\'' +
                ", firstName='" + (user != null ? user.getFirstName() : null) + '\'' +
                ", lastName='" + (user != null ? user.getLastName() : null) + '\'' +
                ", username='" + (user != null ? user.getUsername() : null) + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", isActive=" + (user != null && user.isActive()) +
                '}';
    }
}
