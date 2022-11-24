package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.repository.CourtRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourt;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservation;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservationDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CourtServiceTest {

    @MockBean
    private CourtRepositoryImpl courtRepository;

    @Autowired
    private CourtService courtService;

    @Test
    public void findReservations_givenInvalidNumber_throws() {
        when(courtRepository.findByCourtNumber(4))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> courtService.findReservations(4))
                .withMessage("Court with number 4 not found");
    }

    @Test
    public void findReservations_givenValidNumber_returnsSorted() {
        var reservations = List.of(
                createReservation(1L, time(12, 0), time(13, 30)),
                createReservation(2L, time(14, 0), time(15, 50))
        );
        var court = createCourt(2L, 4, reservations);
        var reservationDtos = List.of(
                createReservationDto(1L, time(12, 0), time(13, 30)),
                createReservationDto(2L, time(14, 0), time(15, 50))
        );

        when(courtRepository.findByCourtNumber(4))
                .thenReturn(Optional.of(court));

        var actual = courtService.findReservations(4);

        assertThat(actual).containsExactlyElementsOf(reservationDtos);
    }

    private LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(2020, 12, 1, hour, minute);
    }
}
