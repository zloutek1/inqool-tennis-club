package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Reservation;

import java.time.LocalDateTime;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    boolean isDateRangeAvailable(int courtNumber, LocalDateTime fromDate, LocalDateTime toDate);

}
