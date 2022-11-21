package cz.inqool.tennis_club_reservation_system.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

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

}
