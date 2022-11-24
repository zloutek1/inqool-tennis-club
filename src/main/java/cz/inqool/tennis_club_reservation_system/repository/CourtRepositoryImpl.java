package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Court;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.Clock;
import java.util.Optional;

@Repository
public class CourtRepositoryImpl extends CrudRepositoryImpl<Court, Long> implements CourtRepository {
    public CourtRepositoryImpl(Clock clock) {
        super(clock, Court.class);
    }

    @Override
    public Optional<Court> findByCourtNumber(int courtNumber) {
        try {
            var foundEntity = entityManager
                    .createQuery("select c from Court c where c.number = :number", Court.class)
                    .setParameter("number", courtNumber)
                    .getSingleResult();
            return Optional.of(foundEntity);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
