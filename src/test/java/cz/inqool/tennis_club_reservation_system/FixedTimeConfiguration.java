package cz.inqool.tennis_club_reservation_system;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

@TestConfiguration
public class FixedTimeConfiguration {

    @Bean
    public Clock clock() {
        ZonedDateTime fixedInstant = ZonedDateTime.of(2019, 4, 15, 14, 0, 0, 0, UTC);
        return Clock.fixed(fixedInstant.toInstant(), fixedInstant.getZone());
    }

}
