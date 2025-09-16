package dev.local.olympia.service.impl;

import dev.local.olympia.domain.Training;
import dev.local.olympia.dto.training.requests.TrainingCreationRequest;
import dev.local.olympia.dto.training.responses.TrainingTypeResponse;
import dev.local.olympia.exception.ResourceNotFoundException;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.interfaces.TrainingDAO;
import dev.local.olympia.interfaces.TrainingTypeDAO;
import dev.local.olympia.service.interfaces.TrainingSessionService;
import jakarta.transaction.Transactional;
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
    private final TrainingTypeDAO trainingTypeDAO;

    @Autowired
    public TrainingManager(TrainingDAO trainingDAO, TraineeDAO traineeDAO, TrainerDAO trainerDAO, TrainingTypeDAO trainingTypeDAO) {
        this.trainingDAO = trainingDAO;
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.trainingTypeDAO = trainingTypeDAO;
        logger.info("TrainingManager initialized with TrainingDAO, TraineeDAO, TrainerDAO, TrainingTypeDAO.");
    }

    @Override
    @Transactional
    public Training createTraining(TrainingCreationRequest request) {
        logger.info("Attempting to create new training: {}", request.getTrainingName());

        if (traineeDAO.findByUsername(request.getTraineeUsername()).isEmpty()) {
            logger.warn("Trainee with Username {} not found for training creation.", request.getTraineeUsername());
            throw new ResourceNotFoundException("Trainee with Username " + request.getTraineeUsername() + " not found.");
        }
        if (trainerDAO.findByUsername(request.getTrainerUsername()).isEmpty()) {
            logger.warn("Trainer with Username {} not found for training creation.", request.getTrainerUsername());
            throw new ResourceNotFoundException("Trainer with Username " + request.getTrainerUsername() + " not found.");
        }

        var trainingType = trainingTypeDAO.findByName(request.getTrainingType());

        var trainee = traineeDAO.findByUsername(request.getTraineeUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainee with Username " + request.getTraineeUsername() + " not found."));
        var trainer = trainerDAO.findByUsername(request.getTrainerUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer with Username " + request.getTrainerUsername() + " not found."));


        Training newTraining = new Training(
                trainee,
                trainer,
                request.getTrainingName(),
                trainingType,
                request.getTrainingDate(),
                request.getTrainingDuration()
        );

        Training savedTraining = trainingDAO.save(newTraining);
        logger.info("Training created successfully with ID: {}", savedTraining.getId());
        return savedTraining;
    }

    @Override
    @Transactional
    public Optional<Training> selectTrainingById(String id) {
        logger.debug("Selecting training by ID: {}", id);
        return trainingDAO.findById(id);
    }

    @Override
    @Transactional
    public List<Training> selectAllTrainings() {
        logger.debug("Selecting all trainings.");
        return trainingDAO.findAll();
    }

    @Override
    @Transactional
    public List<TrainingTypeResponse> trainingTypesList() {
        logger.debug("Retrieving list of training types.");
        return trainingTypeDAO.findAll().stream()
                .map(type -> new TrainingTypeResponse(type.getTrainingTypeName(), type.getId()))
                .toList();
    }
}
