package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.training.TrainingCreationRequest;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import dev.local.olympia.service.interfaces.TrainingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingManager implements TrainingSessionService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingManager.class);

    private final TrainingDAO trainingDAO;
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainingManager(TrainingDAO trainingDAO, TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.trainingDAO = trainingDAO;
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        logger.info("TrainingManager initialized with TrainingDAO, TraineeDAO, TrainerDAO.");
    }

    @Override
    public Training createTraining(TrainingCreationRequest request) {
        logger.info("Attempting to create new training: {}", request.getTrainingName());

        if (!traineeDAO.existsById(request.getTraineeId())) {
            logger.warn("Trainee with ID {} not found for training creation.", request.getTraineeId());
            throw new ResourceNotFoundException("Trainee with ID " + request.getTraineeId() + " not found.");
        }
        if (!trainerDAO.existsById(request.getTrainerId())) {
            logger.warn("Trainer with ID {} not found for training creation.", request.getTrainerId());
            throw new ResourceNotFoundException("Trainer with ID " + request.getTrainerId() + " not found.");
        }

        var trainee = traineeDAO.findById(request.getTraineeId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainee with ID " + request.getTraineeId() + " not found."));
        var trainer = trainerDAO.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer with ID " + request.getTrainerId() + " not found."));


        Training newTraining = new Training(
                trainee,
                trainer,
                request.getTrainingName(),
                request.getTrainingType(),
                request.getTrainingDate(),
                request.getTrainingDuration()
        );

        Training savedTraining = trainingDAO.save(newTraining);
        logger.info("Training created successfully with ID: {}", savedTraining.getId());
        return savedTraining;
    }

    @Override
    public Optional<Training> selectTrainingById(String id) {
        logger.debug("Selecting training by ID: {}", id);
        return trainingDAO.findById(id);
    }

    @Override
    public List<Training> selectAllTrainings() {
        logger.debug("Selecting all trainings.");
        return trainingDAO.findAll();
    }
}
