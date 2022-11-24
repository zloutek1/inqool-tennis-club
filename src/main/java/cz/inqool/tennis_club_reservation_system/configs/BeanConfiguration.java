package cz.inqool.tennis_club_reservation_system.configs;

import cz.inqool.tennis_club_reservation_system.service.DataPopulationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }


    @Bean
    public Supplier<UUID> uuidSupplier() {
        return UUID::randomUUID;
    }

    @Bean
    @ConditionalOnProperty(prefix = "application.data-population", value = "enabled", havingValue = "true")
    public CommandLineRunner commandLineRunner(DataPopulationService dataPopulationService) {
        return args -> {
            log.info("Starting populating the database");

            dataPopulationService.populateDatabase();

            log.info("Finished populating the database");
        };
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
