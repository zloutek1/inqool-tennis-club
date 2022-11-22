package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import cz.inqool.tennis_club_reservation_system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Clock;

import static cz.inqool.tennis_club_reservation_system.model.factory.RefreshTokenFactory.createRefreshToken;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@Import(FixedTimeConfiguration.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Clock clock;

    private User user;

    @BeforeEach
    public void setup() {
        user= createUser(1L, "kyle22");
        user = entityManager.merge(user);
    }

    @Test
    public void findByUser_givenMissingRefreshToken_returnsEmpty() {
        var actual = refreshTokenRepository.findByUser(user);
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByUser_givenExistingRefreshToken_returnsRefreshToken() {
        var refreshToken = createRefreshToken(null, user, "token", clock.instant());
        entityManager.persist(refreshToken);

        var actual = refreshTokenRepository.findByUser(user);

        assertThat(actual).contains(refreshToken);
    }

    @Test
    public void findByToken_givenMissingRefreshToken_returnsEmpty() {
        var actual = refreshTokenRepository.findByToken("missing");
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByToken_givenExistingRefreshToken_returnsRefreshToken() {
        var refreshToken = createRefreshToken(null, user, "token", clock.instant());
        entityManager.persist(refreshToken);

        var actual = refreshTokenRepository.findByToken("token");

        assertThat(actual).contains(refreshToken);
    }

}
