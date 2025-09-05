package dev.local.olympia.controllers;

import dev.local.olympia.dto.training.requests.TrainingCreationRequest;
import dev.local.olympia.dto.training.responses.TrainingTypeResponse;
import dev.local.olympia.service.interfaces.TrainingSessionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {
    private final TrainingSessionService trainingSessionService;

    public TrainingController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    @PostMapping()
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> createTrainingSession(@RequestBody TrainingCreationRequest request) {
        trainingSessionService.createTraining(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/training-types")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        return ResponseEntity.ok(trainingSessionService.trainingTypesList());
    }
}
