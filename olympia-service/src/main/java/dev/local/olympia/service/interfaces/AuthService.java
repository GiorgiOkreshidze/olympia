package dev.local.olympia.service.interfaces;

import dev.local.olympia.dto.auth.AuthCredentials;
import dev.local.olympia.dto.auth.PasswordChangeRequest;

public interface AuthService {
    boolean authenticateUser(AuthCredentials authCredentials);
    boolean changePassword(PasswordChangeRequest request);
}
