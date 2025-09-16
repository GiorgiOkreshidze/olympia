package dev.local.olympia.Security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;

public class BruteForceProtectionFilter extends OncePerRequestFilter {

    private final LoginAttemptService loginAttemptService;
    private final ObjectMapper objectMapper;

    public BruteForceProtectionFilter(LoginAttemptService loginAttemptService, ObjectMapper objectMapper) {
        this.loginAttemptService = loginAttemptService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);

        if (cachedBodyRequest.getRequestURI().contains("/api/auth/login") && "POST".equalsIgnoreCase(cachedBodyRequest.getMethod())) {
            try (InputStream is = cachedBodyRequest.getInputStream()) {
                JsonNode jsonNode = objectMapper.readTree(is);
                String username = jsonNode.get("username").asText();

                if (loginAttemptService.isBlocked(username)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("User temporarily blocked due to failed logins");
                    return;
                }
            } catch (Exception e) {
                // Log the exception, but continue the filter chain
                // in case the request body is not as expected.
                // For production, consider handling this more robustly.
            }
        }
        filterChain.doFilter(cachedBodyRequest, response);
    }
}
