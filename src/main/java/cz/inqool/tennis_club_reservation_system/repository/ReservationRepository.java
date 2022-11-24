package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Reservation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.Clock;

@Repository
public class ReservationRepository extends CrudRepositoryImpl<Reservation, Long> {
    public ReservationRepository(EntityManager entityManager, Clock clock) {
        super(entityManager, clock, Reservation.class);
    }
}
