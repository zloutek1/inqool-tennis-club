package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Reservation;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.Clock;
import java.time.LocalDateTime;

@Repository
public class ReservationRepositoryImpl extends CrudRepositoryImpl<Reservation, Long> implements ReservationRepository {
    public ReservationRepositoryImpl(Clock clock) {
        super(clock, Reservation.class);
    }

    @Override
    public boolean isDateRangeAvailable(int courtNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        String query =
                "select 1 " +
                "from Reservation r " +
                "where r.court.number = :courtNumber and " +
                "      ((:fromDate < r.fromDate and r.fromDate < :toDate) or " +
                "       (:fromDate < r.toDate and r.toDate < :toDate) or" +
                "       (r.fromDate < :fromDate and :toDate < r.toDate) or" +
                "       (r.fromDate = :fromDate or r.toDate = :toDate))";

        try {
            entityManager
                    .createQuery(query, Integer.class)
                    .setParameter("courtNumber", courtNumber)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        }
    }
}
