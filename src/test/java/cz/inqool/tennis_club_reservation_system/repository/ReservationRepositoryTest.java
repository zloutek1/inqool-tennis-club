package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import cz.inqool.tennis_club_reservation_system.model.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservation;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(FixedTimeConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void isDateRangeAvailable_whileAnotherStillGoing_returnsFalse() {
        var reservation = createReservation(1L, time(10, 0), time(12, 15));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isFalse();
    }

    @Test
    void isDateRangeAvailable_whileAnotherStartingInMiddle_returnsFalse() {
        var reservation = createReservation(1L, time(13, 0), time(14, 15));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isFalse();
    }

    @Test
    void isDateRangeAvailable_whileAnotherRunningTheWholeTime_returnsFalse() {
        var reservation = createReservation(1L, time(10, 0), time(20, 0));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isFalse();
    }

    @Test
    void isDateRangeAvailable_whileAnotherStartingAndEndingInMiddle_returnsFalse() {
        var reservation = createReservation(1L, time(12, 0), time(13, 0));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(10, 0), time(20, 15));
        assertThat(actual).isFalse();
    }

    @Test
    void isDateRangeAvailable_reservationSameStartDate_returnsFalse() {
        var reservation = createReservation(1L, time(12, 0), time(20, 0));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isFalse();
    }

    @Test
    void isDateRangeAvailable_reservationSameEndDate_returnsFalse() {
        var reservation = createReservation(1L, time(10, 0), time(13, 15));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isFalse();
    }

    @Test
    void isDateRangeAvailable_givenAvailableRange_returnsTrue() {
        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isTrue();
    }

    @Test
    void isDateRangeAvailable_givenRangeAboutToEnd_returnsTrue() {
        var reservation = createReservation(1L, time(10, 0), time(12, 0));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isTrue();
    }

    @Test
    void isDateRangeAvailable_givenRangeAboutToStart_returnsTrue() {
        var reservation = createReservation(1L, time(13, 15), time(14, 0));
        mergeReservation(reservation);

        entityManager.flush();
        entityManager.clear();

        var actual = reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15));
        assertThat(actual).isTrue();
    }

    private void mergeReservation(Reservation reservation) {
        reservation.getCourt().setNumber(4);
        reservation.getCourt().setTerrain(entityManager.merge(reservation.getCourt().getTerrain()));
        reservation.setCourt(entityManager.merge(reservation.getCourt()));
        reservation.setUser(entityManager.merge(reservation.getUser()));
        entityManager.merge(reservation);
    }

    private LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(2022, 10, 11, hour, minute);
    }

}