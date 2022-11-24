package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.Clock;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends CrudRepositoryImpl<Role, Long> implements RoleRepository {

    public RoleRepositoryImpl(Clock clock) {
        super(clock, Role.class);
    }

    public Optional<Role> findByName(String name) {
        try {
            var foundEntity = entityManager
                    .createQuery("select r from Role r where r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(foundEntity);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
