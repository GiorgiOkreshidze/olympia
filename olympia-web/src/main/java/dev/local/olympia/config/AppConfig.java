package dev.local.olympia.config;

import dev.local.olympia.DataInitializer;
import dev.local.olympia.impl.InMemoryMapStorage;
import dev.local.olympia.impl.InMemoryTraineeDAO;
import dev.local.olympia.impl.InMemoryTrainerDAO;
import dev.local.olympia.impl.InMemoryTrainingDAO;
import dev.local.olympia.interfaces.MapStorage;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {
        "dev.local.olympia.dao.impl",
        "dev.local.olympia.service.impl"
})
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public MapStorage<Object> inMemoryMapStorage() {
        return new InMemoryMapStorage();
    }

    @Bean
    public TraineeDAO inMemoryTraineeDAO(MapStorage<Object> mapStorage) {
        return new InMemoryTraineeDAO(mapStorage);
    }

    @Bean
    public TrainerDAO inMemoryTrainerDAO(MapStorage<Object> mapStorage) {
        return new InMemoryTrainerDAO(mapStorage);
    }

    @Bean
    public TrainingDAO inMemoryTrainingDAO(MapStorage<Object> mapStorage) {
        return new InMemoryTrainingDAO(mapStorage);
    }

    @Bean
    public DataInitializer dataInitializer(MapStorage<Object> mapStorage,
                                           @Value("${app.storage.initialDataPath}") String initialDataPath) {
        var initializer = new DataInitializer(mapStorage);
        initializer.setInitialDataPath(initialDataPath);
        return initializer;
    }
}
