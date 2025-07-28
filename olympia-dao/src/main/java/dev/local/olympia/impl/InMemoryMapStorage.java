package dev.local.olympia.impl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.interfaces.MapStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryMapStorage implements MapStorage<Object> {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryMapStorage.class);
    private final Map<Class<?>, Map<String, ?>> storage = new ConcurrentHashMap<>();

    public InMemoryMapStorage(){
        storage.put(Trainee.class, new ConcurrentHashMap<>());
        storage.put(Trainer.class, new ConcurrentHashMap<>());
        storage.put(Training.class, new ConcurrentHashMap<>());

        logger.info("InMemoryMapStorage initialized with namespaces for Trainee, Trainer, Training.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Map<String, U> getStorage(Class<U> type) {
        return (Map<String, U>) storage.get(type);
    }

    @Override
    public <U> U save(Class<U> entityType, String id, U entity) {
        Map<String, U> storage = getStorage(entityType);
        if (StorageIsNull(storage, entityType)) {
            return null;
        }
        storage.put(id, entity);
        logger.info("Saved entity of type {} with id {}", entityType.getSimpleName(), id);
        return entity;
    }

    @Override
    public <U> Optional<U> findById(Class<U> entityType, String id) {
        Map<String, U> storage = getStorage(entityType);
        if (StorageIsNull(storage, entityType)) {
            return Optional.empty();
        }
        U entity = storage.get(id);
        if (entity == null) {
            logger.info("Entity of type {} with id {} not found", entityType.getSimpleName(), id);
            return Optional.empty();
        }
        logger.info("Found entity of type {} with id {}", entityType.getSimpleName(), id);
        return Optional.of(entity);
    }

    @Override
    public <U> Map<String, U> findAll(Class<U> entityType) {
        Map<String, U> storage = getStorage(entityType);
        logger.debug("Retrieving all {} ({} items).", entityType.getSimpleName(), storage.size());
        return Collections.unmodifiableMap(storage);
    }

    @Override
    public <U> boolean delete(Class<U> entityType, String id) {
        Map<String, U> storage = getStorage(entityType);
        if (StorageIsNull(storage, entityType)) {
            return false;
        }
        if (storage.remove(id) != null) {
            logger.info("Deleted entity of type {} with id {}", entityType.getSimpleName(), id);
            return true;
        }
        return false;
    }

    @Override
    public <U> boolean existsById(Class<U> entityType, String id) {
        Map<String, U> storage = getStorage(entityType);
        if (StorageIsNull(storage, entityType)) {
            return false;
        }
        boolean exists = storage.containsKey(id);
        logger.debug("Checking existence of {} with ID: {}. Exists: {}", entityType.getSimpleName(), id, exists);
        return exists;
    }

    @Override
    public void clearAll() {
        storage.values().forEach(Map::clear);
        logger.warn("All in-memory storage cleared.");
    }

    private <U> boolean StorageIsNull(Map<String, U> map, Class<U> entityType) {
        if (map == null) {
            logger.warn("Storage for entity type {} is null", entityType.getSimpleName());
            return true;
        }
        return false;
    }
}
