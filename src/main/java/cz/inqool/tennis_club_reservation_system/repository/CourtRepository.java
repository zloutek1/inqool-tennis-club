package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Court;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.Clock;

@Repository
public class CourtRepository extends CrudRepositoryImpl<Court, Long> {
    public CourtRepository(EntityManager entityManager, Clock clock) {
        super(entityManager, clock, Court.class);
    }
}
