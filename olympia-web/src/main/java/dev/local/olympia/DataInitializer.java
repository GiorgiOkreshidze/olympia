package dev.local.olympia;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.interfaces.MapStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DataInitializer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final MapStorage<Object> mapStorage;
    private String initialDataPath;

    public DataInitializer(MapStorage<Object> mapStorage) {
        this.mapStorage = mapStorage;
    }

    public void setInitialDataPath(String initialDataPath){
        this.initialDataPath = initialDataPath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Initializing in-memory storage from file: {}", initialDataPath);
        if (initialDataPath == null || initialDataPath.isEmpty()) {
            logger.warn("Initial data path is not set. Skipping data initialization.");
            return;
        }

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(initialDataPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                logger.error("Initial data file not found: {}", initialDataPath);
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                try {
                    processDataLine(line);
                } catch (Exception e) {
                    logger.error("Error processing data line: '{}'. Error: {}", line, e.getMessage());
                }
            }
            logger.info("Data initialization complete.");
        } catch (Exception e) {
            logger.error("Failed to read initial data file: {}. Error: {}", initialDataPath, e.getMessage(), e);
        }
    }

    private void processDataLine(String line) {
        String[] parts = line.split(",");
        String type = parts[0].trim().toLowerCase();

        switch (type) {
            case "trainee":
                if (parts.length >= 5) {
                    Trainee trainee = new Trainee();
                    trainee.setFirstName(parts[1].trim());
                    trainee.setLastName(parts[2].trim());
                    try {
                        trainee.setDateOfBirth(LocalDate.parse(parts[3].trim()));
                    } catch (DateTimeParseException e) {
                        logger.warn("Invalid date format for trainee {}. Using null.", trainee.getFirstName());
                    }
                    trainee.setAddress(parts[4].trim());
                    trainee.setUsername(trainee.getFirstName().toLowerCase() + "." + trainee.getLastName().toLowerCase());
                    trainee.setPassword("password123");
                    mapStorage.save(Trainee.class, trainee.getId(), trainee);
                    logger.debug("Loaded trainee: {}", trainee.getUsername());
                } else {
                    logger.warn("Skipping malformed trainee line: {}", line);
                }
                break;
            case "trainer":
                if (parts.length >= 6) {
                    Trainer trainer = new Trainer();
                    trainer.setFirstName(parts[1].trim());
                    trainer.setLastName(parts[2].trim());
                    trainer.setSpecialization(parts[5].trim());
                    trainer.setUsername(trainer.getFirstName().toLowerCase() + "." + trainer.getLastName().toLowerCase());
                    trainer.setPassword("password123");
                    mapStorage.save(Trainer.class, trainer.getId(), trainer);
                    logger.debug("Loaded trainer: {}", trainer.getUsername());
                } else {
                    logger.warn("Skipping malformed trainer line: {}", line);
                }
                break;
            case "training":
                if (parts.length >= 7) {
                    Training training = new Training();
                    // In a real scenario, you'd look up Trainee/Trainer by ID or username
                    // For this simple initializer, we'll just store the IDs as strings
                    training.setTraineeId(parts[1].trim());
                    training.setTrainerId(parts[2].trim());
                    training.setTrainingName(parts[3].trim());
                    try {
                        training.setTrainingType(TrainingType.valueOf(parts[4].trim().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid training type '{}' for training '{}'. Using OTHER.", parts[4], parts[3]);
                        training.setTrainingType(TrainingType.OTHER);
                    }
                    try {
                        training.setTrainingDate(LocalDate.parse(parts[5].trim()));
                    } catch (DateTimeParseException e) {
                        logger.warn("Invalid date format for training '{}'. Using null.", training.getTrainingName());
                    }
                    try {
                        training.setTrainingDuration(Duration.parse(parts[6].trim())); // e.g., "PT1H30M" for 1h 30m
                    } catch (DateTimeParseException e) {
                        logger.warn("Invalid duration format for training '{}'. Using PT0S.", training.getTrainingName());
                        training.setTrainingDuration(Duration.ZERO);
                    }
                    mapStorage.save(Training.class, training.getId(), training);
                    logger.debug("Loaded training: {}", training.getTrainingName());
                } else {
                    logger.warn("Skipping malformed training line: {}", line);
                }
                break;
            default:
                logger.warn("Unknown data type in initial data file: {}", type);
        }
    }
}
