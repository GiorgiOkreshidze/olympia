package dev.local.olympia.controllers;

import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.trainee.requests.TraineeCreationRequest;
import dev.local.olympia.dto.trainee.requests.TraineeUpdateRequest;
import dev.local.olympia.dto.trainee.responses.TraineeProfileResponse;
import dev.local.olympia.dto.trainer.responses.TrainerResponse;
import dev.local.olympia.dto.training.requests.TraineeTrainingRequest;
import dev.local.olympia.dto.training.responses.TrainingResponse;
import dev.local.olympia.service.interfaces.TraineeService;
import dev.local.olympia.service.interfaces.TrainerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    public TraineeController(
            TraineeService traineeService,
            TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthCredentials> registerTrainee(@RequestBody TraineeCreationRequest request) {
        AuthCredentials response = traineeService.createTrainee(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TraineeProfileResponse> getTrainee(@PathVariable("username") String username) {
        if (traineeService.selectTraineeByUsername(username).isPresent()){
            TraineeProfileResponse traineeProfile = traineeService.selectTraineeByUsername(username).get();
            return ResponseEntity.ok(traineeProfile);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{username}/trainers/unassigned")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TrainerResponse>> getUnassignedTrainers(@PathVariable("username") String traineeUsername) {
        return ResponseEntity.ok(trainerService.findUnassignedTrainers(traineeUsername));
    }

    @GetMapping("/{username}/trainings")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TrainingResponse>> getTraineeTrainings(
            @PathVariable("username") String username,
            @RequestBody TraineeTrainingRequest request){
        List<TrainingResponse> response = traineeService.getTraineeTrainingsList(
                username,
                request.getPeriodFrom(),
                request.getPeriodTo(),
                request.getTrainerName(),
                request.getTrainerName());

        return ResponseEntity.ok(response);
    }

    @PutMapping()
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TraineeProfileResponse> updateTrainee(@RequestBody TraineeUpdateRequest request){
        TraineeProfileResponse trainee = traineeService.updateTrainee(request);
        return ResponseEntity.ok(trainee);
    }

    @PutMapping("/{username}/trainers")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TrainerResponse>> updateTraineeTrainers(@PathVariable("username") String username,
                                                                       @RequestBody List<String> trainerUsernames) {
        var trainers = traineeService.updateTraineeTrainers(username, trainerUsernames);
        return ResponseEntity.ok(trainers);
    }

    @DeleteMapping("/{username}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteTrainee(@PathVariable("username") String username) {
        traineeService.deleteTrainee(username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{username}/{active}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> activateTrainer(@PathVariable("username") String username, @PathVariable("active") boolean isActive) {
        traineeService.activateDeactivateTrainee(username, isActive);
        return ResponseEntity.ok().build();
    }
}
