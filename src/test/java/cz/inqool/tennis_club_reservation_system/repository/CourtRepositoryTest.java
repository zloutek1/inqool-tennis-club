package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourt;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(FixedTimeConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourtRepositoryTest {

    @Autowired
    private CourtRepositoryImpl courtRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void findByCourtNumber_givenMissingCourt_returnsEmpty() {
        var actual = courtRepository.findByCourtNumber(999);
        assertThat(actual).isEmpty();
    }

    @Test
    public void findByCourtNumber_givenExistingCourt_returnsCourt() {
        var court = createCourt(null, 4);
        entityManager.merge(court.getTerrain());
        entityManager.persist(court);

        var actual = courtRepository.findByCourtNumber(4);

        assertThat(actual).contains(court);
    }

}
