package dev.local.olympia.interfaces;

import dev.local.olympia.domain.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    Trainer save(Trainer trainer);
    Optional<Trainer> findById(String id);
    List<Trainer> findAll();
    boolean existsById(String id);
    Optional<Trainer> findByUsername(String username); // Added for username uniqueness check
    List<Trainer> findByFirstNameAndLastName(String firstName, String lastName); // Added for username generation logic
}
