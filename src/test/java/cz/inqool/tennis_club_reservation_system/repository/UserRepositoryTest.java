package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(FixedTimeConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findByUsername_givenMissingUser_returnsEmpty() {
        var actual = userRepository.findByUsername("missing");
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByUsername_givenExistingUser_returnsUser() {
        var user = createUser(null, "billy22");
        entityManager.persist(user);

        var actual = userRepository.findByUsername("billy22");

        assertThat(actual).contains(user);
    }

    @Test
    public void findByPhoneNumber_givenMissingUser_returnsEmpty() {
        var actual = userRepository.findByPhoneNumber("00999999");
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByPhoneNumber_givenExistingUser_returnsUser() {
        var user = createUser(null,  "00546789", "billy22", "pass");
        entityManager.persist(user);

        var actual = userRepository.findByPhoneNumber("00546789");

        assertThat(actual).contains(user);
    }

}
