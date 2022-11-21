package cz.inqool.tennis_club_reservation_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TennisClubReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennisClubReservationApplication.class, args);
    }

}
