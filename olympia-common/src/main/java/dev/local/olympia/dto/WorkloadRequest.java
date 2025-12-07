package dev.local.olympia.dto; // Adjust package to match yours

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class WorkloadRequest {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDate trainingDate;
    private Long trainingDuration;
    private String actionType; // "ADD" or "DELETE"
}
