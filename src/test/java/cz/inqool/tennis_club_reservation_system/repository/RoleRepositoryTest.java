package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static cz.inqool.tennis_club_reservation_system.model.factory.RoleFactory.createRole;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(FixedTimeConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findByName_givenMissingRole_returnsEmpty() {
        var actual = roleRepository.findByName("missing");
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByName_givenExistingRole_returnsRole() {
        var role = createRole(null, "USER");
        entityManager.persist(role);

        var actual = roleRepository.findByName("USER");

        assertThat(actual).contains(role);
    }

}
