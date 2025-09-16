package dev.local.olympia.domain;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration", nullable = false)
    private long trainingDurationSeconds;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "trainee_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_training_trainee")
    )
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "trainer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_training_trainer")
    )
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "training_type_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_training_type")
    )
    private TrainingType trainingType;


    public Training(){
    }

    public Training(Trainee trainee, Trainer trainer, String trainingName,
                    TrainingType trainingType, LocalDate trainingDate, Duration trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDurationSeconds = trainingDuration != null ? trainingDuration.getSeconds() : 0L;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public Duration getTrainingDuration() {
        return Duration.ofSeconds(trainingDurationSeconds);
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }
    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setTrainingDuration(Duration trainingDuration) {
        this.trainingDurationSeconds = trainingDuration != null ? trainingDuration.getSeconds() : 0L;
    }

    // Setter for Hibernate to directly set the seconds
    public void setTrainingDurationSeconds(Long trainingDurationSeconds) {
        this.trainingDurationSeconds = trainingDurationSeconds;
    }

    // Getter for Hibernate to directly get the seconds
    public Long getTrainingDurationSeconds() {
        return trainingDurationSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(id, training.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id='" + id + '\'' +
                ", traineeId='" + (trainee != null ? trainee.getUser().getId() : "null") + '\'' +
                ", trainerId='" + (trainer != null ? trainer.getUser().getId() : "null") + '\'' +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + (trainingType != null ? trainingType.getTrainingTypeName() : "null") +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + getTrainingDurationSeconds() +
                '}';
    }
}
