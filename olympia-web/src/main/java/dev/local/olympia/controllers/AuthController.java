package dev.local.olympia.controllers;

import dev.local.olympia.Security.JwtUtil;
import dev.local.olympia.Security.LoginAttemptService;
import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.auth.PasswordChangeRequest;
import dev.local.olympia.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final LoginAttemptService loginAttemptService;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtUtil jwtUtil, LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthCredentials authCredentials) {
        boolean isAuthenticated = authService.authenticateUser(authCredentials);
        try {
            // Step 2: Delegate authentication to Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authCredentials.getUsername(),
                            authCredentials.getPassword()
                    )
            );

            // Step 3: If authentication succeeds, update the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Step 4: Clear failed login attempts and generate token
            loginAttemptService.loginSucceeded(authCredentials.getUsername());
            String token = jwtUtil.generateToken(authCredentials.getUsername());

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            // Step 5: If authentication fails, log the failed attempt
            loginAttemptService.loginFailed(authCredentials.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PutMapping("/login")

    public ResponseEntity<String> updateLogin(@Valid @RequestBody PasswordChangeRequest request) {
        boolean isChanged = authService.changePassword(request);

        if (isChanged) {
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to change password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Logs out the authenticated user by invalidating the session.")
    public ResponseEntity<String> logout() {
        // The actual logout is handled by Spring Security's LogoutFilter.
        // This endpoint is just for documentation and a clean response.
        return ResponseEntity.ok("Successfully logged out.");
    }
}
