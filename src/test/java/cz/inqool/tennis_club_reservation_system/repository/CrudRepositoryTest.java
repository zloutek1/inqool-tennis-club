package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import cz.inqool.tennis_club_reservation_system.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.Clock;
import java.time.LocalDateTime;

import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import(FixedTimeConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CrudRepositoryTest {

    @Autowired
    private CrudRepository<User, Long> userRepository;

    @Autowired
    private Clock clock;

    @PersistenceContext
    private EntityManager em;

    @Test
    void findAll_givenNoUsers_returnsEmpty() {
        var actual = userRepository.findAll(PageRequest.of(0, 10));
        assertThat(actual).isEmpty();
    }

    @Test
    void findAll_givenTwoUsers_returnsTwo() {
        em.merge(createUser(1L, "742 100 1181",  "bob", "pass"));
        em.merge(createUser(2L, "314 971 2520", "kayle", "pass2"));

        var actual = userRepository.findAll(PageRequest.of(0, 10));

        assertThat(actual)
                .extracting(User::getPhoneNumber)
                .containsExactlyInAnyOrder("742 100 1181", "314 971 2520");
    }

    @Test
    void findById_givenMissingUser_returnsEmpty() {
        var actual = userRepository.findById(999L);
        assertThat(actual).isEmpty();
    }

    @Test
    void findById_givenSoftDeletedUser_returnsEmpty() {
        var user = createUser(1L, "442 653 7186", "john");
        user.setDeletedAt(LocalDateTime.now(clock));
        em.merge(user);

        var actual = userRepository.findById(1L);

        assertThat(actual).isEmpty();
    }

    @Test
    void findById_givenExistingUser_returnsUser() {
        var user = em.merge(createUser(1L, "442 653 7186", "jimmy"));

        var actual = userRepository.findById(1L);

        assertThat(actual).contains(user);
    }

    @Test
    void save_givenExisingUser_throws() {
        em.merge(createUser(1L, "442 653 7186", "ola"));
        var user = createUser(null, "442 653 7186", "ola");

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> userRepository.save(user));
    }

    @Test
    void save_givenNewUser_saves() {
        var user = createUser(null, "442 653 7186", "frank");

        var savedUser = userRepository.save(user);

        assertThat(em.find(User.class, savedUser.getId())).isNotNull();
    }

    @Test
    void update_givenMissingUser_throws() {
        var user = createUser(null, "175 183 0008", "george");

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> userRepository.update(user));
    }

    @Test
    void update_givenUpdatedUser_updates() {
        var user = createUser(null, "175-183-0008", "fatima");
        em.persist(user);

        user.setPhoneNumber("643 175 5572");
        userRepository.update(user);
        em.flush();
        em.clear();

        var foundUser = em.find(User.class, user.getId());
        assertThat(foundUser.getPhoneNumber()).isEqualTo("643 175 5572");
    }

    @Test
    void softDeleteById_givenMissingId_throws() {
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> userRepository.softDeleteById(999L));
    }

    @Test
    void softDeleteById_givenExistingId_deletes() {
        var user = createUser(null, "643 175 5572", "olga");
        em.persist(user);

        userRepository.softDeleteById(user.getId());
        em.flush();
        em.clear();

        var foundUser = em.find(User.class, user.getId());
        assertThat(foundUser.getDeletedAt()).isEqualTo(LocalDateTime.now(clock));
    }
}
