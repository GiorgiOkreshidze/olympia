package dev.local.olympia.impl;

import dev.local.olympia.interfaces.MapStorage;

import java.util.Map;
import java.util.Optional;

public class InMemoryMapStorage implements MapStorage<Object> {
    @Override
    public <U> Map<String, U> getStorage(Class<U> type) {
        return Map.of();
    }

    @Override
    public <U> U save(Class<U> entityType, String id, U entity) {
        return null;
    }

    @Override
    public <U> Optional<U> findById(Class<U> entityType, String id) {
        return Optional.empty();
    }

    @Override
    public <U> Map<String, U> findAll(Class<U> entityType) {
        return Map.of();
    }

    @Override
    public <U> boolean delete(Class<U> entityType, String id) {
        return false;
    }

    @Override
    public <U> boolean existsById(Class<U> entityType, String id) {
        return false;
    }

    @Override
    public void clearAll() {

    }
}
