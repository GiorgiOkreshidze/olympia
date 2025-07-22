package dev.local.olympia.interfaces;

import dev.local.olympia.domain.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {
    Trainee save(Trainee trainee);
    Optional<Trainee> findById(String id);
    List<Trainee> findAll();
    void delete(String id);
    boolean existsById(String id);
    Optional<Trainee> findByUsername(String username); // Added for username uniqueness check
    List<Trainee> findByFirstNameAndLastName(String firstName, String lastName); // Added for username generation logic
}
