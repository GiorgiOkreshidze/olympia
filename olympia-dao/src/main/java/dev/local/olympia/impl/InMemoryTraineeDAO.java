package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.interfaces.TraineeDAO;

import java.util.List;
import java.util.Optional;

public class InMemoryTraineeDAO implements TraineeDAO {
    @Override
    public Trainee save(Trainee trainee) {
        return null;
    }

    @Override
    public Optional<Trainee> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Trainee> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        return List.of();
    }
}
