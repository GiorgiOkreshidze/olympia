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

        logger.info("\n--- Demonstrating CRUD Operations ---");

        logger.info("Creating Trainees...");
        Trainee trainee1 = traineeService.createTrainee(new TraineeCreationRequest(
                "Alice", "Wonder", LocalDate.of(1995, 1, 1), "101 Wonderland Rd"));
        Trainee trainee2 = traineeService.createTrainee(new TraineeCreationRequest(
                "Bob", "Builder", LocalDate.of(1988, 7, 10), "202 Construction Site"));
        Trainee trainee3 = traineeService.createTrainee(new TraineeCreationRequest(
                "Alice", "Wonder", LocalDate.of(1996, 2, 2), "101 Wonderland Rd (Duplicate)")); // Test duplicate username

        logger.info("Creating Trainers...");
        Trainer trainer1 = trainerService.createTrainer(new TrainerCreationRequest(
                "Charlie", "Chaplin", "Pilates"));
        Trainer trainer2 = trainerService.createTrainer(new TrainerCreationRequest(
                "Diana", "Prince", "Strength"));

        logger.info("\n--- All Trainees ---");
        List<Trainee> allTrainees = traineeService.selectAllTrainees();
        allTrainees.forEach(t -> logger.info("Trainee: {}", t.getUsername()));

        logger.info("\n--- All Trainers ---");
        List<Trainer> allTrainers = trainerService.selectAllTrainers();
        allTrainers.forEach(t -> logger.info("Trainer: {}", t.getUsername()));

        logger.info("\n--- Select by ID ---");
        Optional<Trainee> foundTrainee = traineeService.selectTraineeById(trainee1.getId());
        foundTrainee.ifPresent(t -> logger.info("Found Trainee by ID: {}", t.getUsername()));

        logger.info("\n--- Select by Username ---");
        Optional<Trainer> foundTrainer = trainerService.selectTrainerByUsername("diana.prince");
        foundTrainer.ifPresent(t -> logger.info("Found Trainer by Username: {}", t.getUsername()));

        logger.info("\n--- Creating Trainings ---");
        try {
            Training training1 = trainingSessionService.createTraining(new TrainingCreationRequest(
                    trainee1.getId(), trainer1.getId(), "Morning Pilates", TrainingType.FLEXIBILITY,
                    LocalDate.of(2025, 7, 25), Duration.ofHours(1)));
            logger.info("Created training: {}", training1.getTrainingName());

            Training training2 = trainingSessionService.createTraining(new TrainingCreationRequest(
                    trainee2.getId(), trainer2.getId(), "Evening Strength", TrainingType.STRENGTH,
                    LocalDate.of(2025, 7, 26), Duration.ofMinutes(90)));
            logger.info("Created training: {}", training2.getTrainingName());
        } catch (ResourceNotFoundException e) {
            logger.error("Failed to create training: {}", e.getMessage());
        }


        logger.info("\n--- All Trainings ---");
        List<Training> allTrainings = trainingSessionService.selectAllTrainings();
        allTrainings.forEach(t -> logger.info("Training: {}", t.getTrainingName()));

        logger.info("\n--- Updating Trainee ---");
        TraineeUpdateRequest updateRequest = new TraineeUpdateRequest(trainee1.getId(), "Alicia", "Wonderland", null, "New Wonderland Address", false);
        try {
            Trainee updatedTrainee = traineeService.updateTrainee(updateRequest);
            logger.info("Updated Trainee: {} (Active: {})", updatedTrainee.getUsername(), updatedTrainee.isActive());
        } catch (ResourceNotFoundException e) {
            logger.error("Failed to update trainee: {}", e.getMessage());
        }


        logger.info("\n--- Deleting Trainee ---");
        try {
            traineeService.deleteTrainee(trainee2.getId());
            logger.info("Trainee {} deleted.", trainee2.getUsername());
        } catch (ResourceNotFoundException e) {
            logger.error("Failed to delete trainee: {}", e.getMessage());
        }

        logger.info("\n--- All Trainees After Deletion ---");
        traineeService.selectAllTrainees().forEach(t -> logger.info("Trainee: {}", t.getUsername()));

        logger.info("Olympia finished.");

        context.close();
    }
}