package dev.local.olympia.impl;

import dev.local.olympia.domain.Training;
import dev.local.olympia.interfaces.TrainingDAO;

import java.util.List;
import java.util.Optional;

public class InMemoryTrainingDAO implements TrainingDAO {
    @Override
    public Training save(Training training) {
        return null;
    }

    @Override
    public Optional<Training> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Training> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public boolean existsById(String id) {
        return false;
    }
}
