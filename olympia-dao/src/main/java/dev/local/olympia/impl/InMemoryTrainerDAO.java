package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.interfaces.TrainerDAO;

import java.util.List;
import java.util.Optional;

public class InMemoryTrainerDAO implements TrainerDAO {
    @Override
    public Trainer save(Trainer trainer) {
        return null;
    }

    @Override
    public Optional<Trainer> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Trainer> findAll() {
        return List.of();
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        return List.of();
    }
}
