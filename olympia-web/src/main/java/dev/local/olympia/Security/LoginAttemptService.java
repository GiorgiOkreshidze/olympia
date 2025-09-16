package dev.local.olympia.Security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 3;
    private final long BLOCK_TIME_MS = 5 * 60 * 1000;

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        Attempt attempt = attempts.getOrDefault(username, new Attempt());
        attempt.count++;
        if (attempt.count >= MAX_ATTEMPT) {
            attempt.blockedUntil = Instant.now().plusMillis(BLOCK_TIME_MS);
        }
        attempts.put(username, attempt);
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
    }

    public boolean isBlocked(String username) {
        Attempt attempt = attempts.get(username);
        return attempt != null && attempt.blockedUntil != null && Instant.now().isBefore(attempt.blockedUntil);
    }

    private static class Attempt {
        int count = 0;
        Instant blockedUntil;
    }
}

