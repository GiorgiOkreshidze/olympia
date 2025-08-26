package dev.local.olympia.controllers;

import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.auth.PasswordChangeRequest;
import dev.local.olympia.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthCredentials authCredentials) {
        boolean isAuthenticated = authService.authenticateUser(authCredentials);

        if (isAuthenticated) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/login")
    public ResponseEntity<String> updateLogin(@RequestBody PasswordChangeRequest request) {
        boolean isChanged = authService.changePassword(request);

        if (isChanged) {
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to change password", HttpStatus.BAD_REQUEST);
        }
    }
}
