package dev.local.olympia.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.function.Supplier;

@Component
public class CustomMetrics {

    private final MeterRegistry meterRegistry;
    private Counter trainerCreated;
    private Counter loginFailed;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        trainerCreated = Counter.builder("trainer.created.count")
                .description("Number of created trainers")
                .register(meterRegistry);

        loginFailed = Counter.builder("trainee.login.failed.count")
                .description("Number of failed trainee logins")
                .register(meterRegistry);
    }

    public void trainerCreated() { trainerCreated.increment(); }
    public void traineeLoginFailed() { loginFailed.increment(); }

    // helper for timing
    public <T> T time(String name, Supplier<T> supplier) {
        Timer.Sample s = Timer.start(meterRegistry);
        try { return supplier.get(); }
        finally { s.stop(meterRegistry.timer(name)); }
    }
}
