package dev.local.olympia.service.impl;

import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.auth.PasswordChangeRequest;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.service.interfaces.AuthService;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.service.interfaces.TrainerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthManager implements AuthService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    public AuthManager(
            TraineeDAO traineeDAO,
            TrainerDAO trainerDAO,
            TraineeService traineeService,
            TrainerService TrainerService
    ) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.traineeService = traineeService;
        this.trainerService = TrainerService;
    }

    @Override
    @Transactional
    public boolean authenticateUser(AuthCredentials authCredentials) {
        var username = authCredentials.getUsername();
        var password = authCredentials.getPassword();

        if (traineeDAO.findByUsername(username).isPresent()) {
            return traineeDAO.findByUsername(username).get().getUser().getPassword().equals(password);
        }

        if (trainerDAO.findByUsername(username).isPresent()) {
            return trainerDAO.findByUsername(username).get().getUser().getPassword().equals(password);
        }

        return false;
    }

    @Override
    @Transactional
    public boolean changePassword(PasswordChangeRequest request) {
        if(traineeDAO.findByUsername(request.getUsername()).isPresent()){
            var credentials = new AuthCredentials(request.getUsername(), request.getOldPassword());
            var trainee = traineeDAO.findByUsername(request.getUsername()).get();
            traineeService.updateTraineePassword(trainee.getUser().getId(), request.getNewPassword());

            return true;
        }

        if (trainerDAO.findByUsername(request.getUsername()).isPresent()) {
            var credentials = new AuthCredentials(request.getUsername(), request.getOldPassword());
            var trainer = trainerDAO.findByUsername(request.getUsername()).get();
            trainerService.updateTrainerPassword(trainer.getUser().getId(), request.getNewPassword());

            return true;
        }

        return false;
    }
}
