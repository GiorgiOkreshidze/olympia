package dev.local.olympia.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest; // Use javax.servlet if on Spring Boot 2
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 1. Get the current HTTP Request coming into Olympia
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // 2. Forward the Authorization Header (JWT)
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    template.header("Authorization", authHeader);
                }

                // 3. Forward the Transaction ID (from Header or MDC)
                // Assuming your Logging Filter puts it in MDC, or we take it from header
                String transactionId = request.getHeader("transactionId");
                if (transactionId == null) {
                    transactionId = MDC.get("transactionId");
                }

                if (transactionId != null) {
                    template.header("transactionId", transactionId);
                }
            }
        };
    }
}
