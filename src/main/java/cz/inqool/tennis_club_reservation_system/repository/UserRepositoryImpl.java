package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.Clock;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends CrudRepositoryImpl<User, Long> implements UserRepository {
    public UserRepositoryImpl(Clock clock) {
        super(clock, User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            var foundEntity = entityManager
                    .createQuery("select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(foundEntity);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        try {
            var foundEntity = entityManager
                    .createQuery("select u from User u where u.phoneNumber = :phoneNumber", User.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            return Optional.of(foundEntity);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
