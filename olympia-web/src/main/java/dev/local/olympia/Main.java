package dev.local.olympia;

import dev.local.olympia.config.AppConfig;
import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.dto.trainee.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.TraineeUpdateRequest;
import dev.local.olympia.dto.trainer.TrainerCreationRequest;
import dev.local.olympia.dto.training.TrainingCreationRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.service.interfaces.TrainerService;
import dev.local.olympia.service.interfaces.TrainingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Olympia");

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        logger.info("Spring Application Context initialized.");

        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingSessionService trainingSessionService = context.getBean(TrainingSessionService.class);

        try {
            // Create a new trainer
            TrainerCreationRequest trainerRequest = new TrainerCreationRequest(
                    "Jane", "Doe", "Yoga Specialist"
            );
            Trainer newTrainer = trainerService.createTrainer(trainerRequest);
            logger.info("Created new trainer: {}", newTrainer);


        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());

        } catch (Exception e) {
            logger.error("An error occurred during application run: {}", e.getMessage(), e);
        }

        context.close();
    }
}