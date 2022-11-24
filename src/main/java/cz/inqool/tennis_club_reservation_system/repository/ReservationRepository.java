package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Reservation;
import org.springframework.stereotype.Repository;

import java.time.Clock;

@Repository
public class ReservationRepository extends CrudRepositoryImpl<Reservation, Long> {
    public ReservationRepository(Clock clock) {
        super(clock, Reservation.class);
    }
}
