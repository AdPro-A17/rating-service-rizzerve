package id.ac.ui.cs.advprog.rating_service.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter ratingCreatedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("rating.created.count")
                .description("Number of ratings created")
                .register(meterRegistry);
    }

    @Bean
    public Timer ratingCreationTimer(MeterRegistry meterRegistry) {
        return Timer.builder("rating.creation.time")
                .description("Time taken to create a rating")
                .register(meterRegistry);
    }

    @Bean
    public Counter ratingDeletedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("rating.deleted.count")
                .description("Number of ratings deleted")
                .register(meterRegistry);
    }

    @Bean
    public Timer ratingDeletionTimer(MeterRegistry meterRegistry) {
        return Timer.builder("rating.deletion.time")
                .description("Time taken to delete a rating")
                .register(meterRegistry);
    }
}
