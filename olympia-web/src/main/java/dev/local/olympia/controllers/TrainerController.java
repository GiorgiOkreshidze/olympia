package dev.local.olympia.controllers;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.trainer.requests.TrainerCreationRequest;
import dev.local.olympia.dto.trainer.requests.TrainerUpdateRequest;
import dev.local.olympia.dto.trainer.responses.TrainerProfileResponse;
import dev.local.olympia.dto.training.requests.TrainerTrainingRequest;
import dev.local.olympia.dto.training.responses.TrainingResponse;
import dev.local.olympia.metrics.CustomMetrics;
import dev.local.olympia.service.interfaces.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    private final CustomMetrics metrics;
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService, CustomMetrics metrics) {
        this.metrics = metrics;
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthCredentials> registerTrainer(@RequestBody TrainerCreationRequest request) {
        AuthCredentials response = trainerService.createTrainer(request);
        metrics.trainerCreated();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponse> getTrainer(@PathVariable("username") String username){
        if (trainerService.selectTrainerByUsername(username).isPresent()) {
            TrainerProfileResponse trainerProfile = trainerService.selectTrainerByUsername(username).get();
            return ResponseEntity.ok(trainerProfile);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping({"/{username}/trainings"})
    public ResponseEntity<List<TrainingResponse>> getTrainerTrainings(
            @PathVariable("username") String username,
            @RequestBody TrainerTrainingRequest request) {
        List<TrainingResponse> response = trainerService.getTrainerTrainingsList(username, request.getPeriodFrom(), request.getPeriodTo(), request.getTraineeName());
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    public ResponseEntity<TrainerProfileResponse> updateTrainer(@RequestBody TrainerUpdateRequest request) {
        TrainerProfileResponse trainerProfile = trainerService.updateTrainer(request);
        return ResponseEntity.ok(trainerProfile);
    }

    @PatchMapping("/{username}/{active}")
    public ResponseEntity<Void> activateTrainer(@PathVariable("username") String username, @PathVariable("active") boolean isActive) {
        trainerService.activateDeactivateTrainer(username, isActive);
        return ResponseEntity.ok().build();
    }
}
