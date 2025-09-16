package dev.local.olympia.interfaces;

import dev.local.olympia.domain.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {
    Trainee save(Trainee trainee);

    Optional<Trainee> findById(String id);
    Optional<Trainee> findByUsername(String username);
    List<Trainee> findAll();

    void delete(Trainee Trainee);

    boolean existsById(String id);
}
