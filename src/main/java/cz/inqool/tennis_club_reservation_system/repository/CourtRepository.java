package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Court;

import java.util.Optional;

public interface CourtRepository extends CrudRepository<Court, Long> {

    Optional<Court> findByCourtNumber(int courtNumber);

}
