package dev.local.olympia;

import dev.local.olympia.config.AppConfig;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.service.interfaces.TrainerService;
import dev.local.olympia.service.interfaces.TrainingSessionService;
import dev.local.olympia.util.PasswordGenerator;
import dev.local.olympia.util.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Olympia");

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        logger.info("Spring Application Context initialized.");

        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingSessionService trainingSessionService = context.getBean(TrainingSessionService.class);
        UsernameGenerator usernameGenerator = context.getBean(UsernameGenerator.class);
        PasswordGenerator passwordGenerator = context.getBean(PasswordGenerator.class);

        try {

        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());

        } catch (Exception e) {
            logger.error("An error occurred during application run: {}", e.getMessage(), e);
        }

        context.close();
    }
}