package cz.inqool.tennis_club_reservation_system;

import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ClockProvider;
import javax.validation.Configuration;
import java.time.Clock;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

@TestConfiguration
public class FixedTimeConfiguration {

    @Value("${hibernate.validator.temporal_validation_tolerance:60000}")
    private int temporalValidationTolerance;

    @Bean
    public Clock clock() {
        ZonedDateTime fixedInstant = ZonedDateTime.of(2019, 4, 15, 14, 0, 0, 0, UTC);
        return Clock.fixed(fixedInstant.toInstant(), fixedInstant.getZone());
    }

    @Bean
    public ClockProvider clockProvider() {
            return this::clock;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean() {
            @Override
            protected void postProcessConfiguration(Configuration<?> configuration) {
                configuration.clockProvider(clockProvider());
                configuration.addProperty(HibernateValidatorConfiguration.TEMPORAL_VALIDATION_TOLERANCE,
                                    String.valueOf(temporalValidationTolerance));
            }
        };
    }

}
