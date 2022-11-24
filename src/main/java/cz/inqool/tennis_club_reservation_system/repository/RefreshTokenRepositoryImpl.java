package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.RefreshToken;
import cz.inqool.tennis_club_reservation_system.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.Clock;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl extends CrudRepositoryImpl<RefreshToken, Long> implements RefreshTokenRepository {

    public RefreshTokenRepositoryImpl(Clock clock) {
        super(clock, RefreshToken.class);
    }

    public Optional<RefreshToken> findByUser(User user) {
        try {
            var foundEntity = entityManager
                    .createQuery("select t from RefreshToken t where t.user.id = :user_id", RefreshToken.class)
                    .setParameter("user_id", user.getId())
                    .getSingleResult();
            return Optional.of(foundEntity);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        try {
            var foundEntity = entityManager
                    .createQuery("select t from RefreshToken t where t.token = :token", RefreshToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(foundEntity);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

}
