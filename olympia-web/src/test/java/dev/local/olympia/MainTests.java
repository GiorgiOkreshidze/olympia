package dev.local.olympia;

import dev.local.olympia.config.AppConfig;
import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.trainee.requests.TraineeCreationRequest;
import dev.local.olympia.dto.trainer.requests.TrainerCreationRequest;
import dev.local.olympia.dto.training.requests.TrainingCreationRequest;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.service.interfaces.TrainerService;
import dev.local.olympia.service.interfaces.TrainingSessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MainTests {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("Spring Application Context should load successfully")
    void contextLoads() {
        assertNotNull(context, "Application context should not be null after loading");
        assertDoesNotThrow(() -> context.getBean(TraineeService.class), "TraineeProfileService bean should be available");
        assertDoesNotThrow(() -> context.getBean(TrainerService.class), "TrainerProfileService bean should be available");
        assertDoesNotThrow(() -> context.getBean(TrainingSessionService.class), "TrainingSessionService bean should be available");
        assertDoesNotThrow(() -> context.getBean(TraineeDAO.class), "TraineeDAO bean should be available");
        assertDoesNotThrow(() -> context.getBean(TrainerDAO.class), "TrainerDAO bean should be available");
        assertDoesNotThrow(() -> context.getBean(TrainingDAO.class), "TrainingDAO bean should be available");
    }

    /*@Test
    @DisplayName("Services should work end-to-end (basic flow)")
    void servicesWorkEndToEnd() {
        TraineeService traineeProfileService = context.getBean(TraineeService.class);
        TrainerService trainerProfileService = context.getBean(TrainerService.class);
        TrainingSessionService trainingSessionService = context.getBean(TrainingSessionService.class);

        var credentialsTrainee = traineeProfileService.createTrainee(new TraineeCreationRequest(
                "New", "User", LocalDate.of(2000, 1, 1), "New Address"));

        var authCredentialsTrainee = new AuthCredentials(
                credentialsTrainee.getUsername(),
                credentialsTrainee.getPassword()
        );

        var credentialsTrainer = trainerProfileService.createTrainer(new TrainerCreationRequest(
                "trainer",
                "lastName",
                "Yoga"
        ));

        var authCredentialsTrainer = new AuthCredentials(
                credentialsTrainer.getUsername(),
                credentialsTrainer.getPassword()
        );

        int initialTraineeCount = traineeProfileService.selectAllTrainees().size();

        var credentialsTrainee2 = traineeProfileService.createTrainee(new TraineeCreationRequest(
                "New", "User", LocalDate.of(2000, 1, 1), "New Address"));

        assertEquals(initialTraineeCount + 1, traineeProfileService.selectAllTrainees().size());

        assertNotNull(traineeProfileService.selectTraineeById(
                traineeProfileService.selectAllTrainees().getFirst().getUser().getId())
        );

        if (!traineeProfileService.selectAllTrainees().isEmpty() &&
                !trainerProfileService.selectAllTrainers().isEmpty()) {
            String traineeId = traineeProfileService.selectAllTrainees().getFirst().getUser().getId();
            String trainerId = trainerProfileService.selectAllTrainers().getFirst().getUser().getId();
            assertDoesNotThrow(() -> trainingSessionService.createTraining(
                    new TrainingCreationRequest(
                            traineeId, trainerId, "Integration Test Training", "Yoga", LocalDate.now(), Duration.ofHours(1)
                    )
            ));
        }

        traineeProfileService.deleteTrainee(credentialsTrainee2.getUsername());
        traineeProfileService.deleteTrainee(credentialsTrainee.getUsername());
    }*/
}