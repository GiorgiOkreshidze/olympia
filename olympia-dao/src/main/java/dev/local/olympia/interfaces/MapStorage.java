package dev.local.olympia.interfaces;

import java.util.Map;
import java.util.Optional;

public interface MapStorage<T> {
    <U extends T> Map<String, U> getStorage(Class<U> type);
    <U extends T> U save(Class<U> entityType, String id, U entity);
    <U extends T> Optional<U> findById(Class<U> entityType, String id);
    <U extends T> Map<String, U> findAll(Class<U> entityType);
    <U extends T> boolean delete(Class<U> entityType, String id);
    <U extends T> boolean existsById(Class<U> entityType, String id);
    void clearAll();
}
