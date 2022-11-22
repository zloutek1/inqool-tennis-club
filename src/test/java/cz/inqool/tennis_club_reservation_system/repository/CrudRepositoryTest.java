package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CrudRepositoryTest {

    @Autowired
    private CrudRepository<User, Long> userRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void findAll_givenNoUsers_returnsEmpty() {
        var actual = userRepository.findAll(PageRequest.of(0, 10));
        assertThat(actual).isEmpty();
    }

    @Test
    void findAll_givenTwoUsers_returnsTwo() {
        em.merge(createUser(1L, "bob"));
        em.merge(createUser(2L, "scott"));

        var actual = userRepository.findAll(PageRequest.of(0, 10));

        assertThat(actual)
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("bob", "scott");
    }

    @Test
    void findById_givenMissingUser_returnsEmpty() {
        var actual = userRepository.findById(999L);
        assertThat(actual).isEmpty();
    }

    @Test
    void findById_givenExistingUser_returnsUser() {
        var user = em.merge(createUser(1L, "travis"));

        var actual = userRepository.findById(1L);

        assertThat(actual).contains(user);
    }

    @Test
    void save_givenExisingUser_throws() {
        em.merge(createUser(1L, "travis"));
        var user = createUser(null, "travis");

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> userRepository.save(user));
    }

    @Test
    void save_givenNewUser_saves() {
        var user = createUser(null, "travis");

        var savedUser = userRepository.save(user);

        assertThat(em.find(User.class, savedUser.getId())).isNotNull();
    }

    @Test
    void update_givenMissingUser_throws() {
        var user = createUser(null, "bob");

        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> userRepository.update(user));
    }

    @Test
    void update_givenUpdatedUser_updates() {
        var user = createUser(null, "john");
        em.persist(user);

        user.setUsername("billy");
        userRepository.update(user);
        em.flush();
        em.clear();

        var foundUser = em.find(User.class, user.getId());
        assertThat(foundUser.getUsername()).isEqualTo("billy");
    }

    @Test
    void deleteById_givenMissingId_throws() {
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> userRepository.deleteById(999L));
    }

    @Test
    void deleteById_givenExistingId_deletes() {
        var user = createUser(null, "john");
        em.persist(user);

        userRepository.deleteById(user.getId());
        em.flush();
        em.clear();

        var foundUser = em.find(User.class, user.getId());
        assertThat(foundUser).isNull();
    }
}
