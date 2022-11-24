package cz.inqool.tennis_club_reservation_system.validator;

import cz.inqool.tennis_club_reservation_system.repository.ReservationRepositoryImpl;
import org.hibernate.validator.internal.util.annotation.AnnotationDescriptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourtDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservationCreateDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReservationDateAvailableValidatorTest {

    @MockBean
    private ReservationRepositoryImpl reservationRepository;

    @Test
    public void isValid_givenAvailableReservation_returnsTrue() {
        var validator = new ReservationDateAvailableValidator(reservationRepository);
        var constraintAnnotation = new AnnotationDescriptor.Builder<>(ReservationDateAvailable.class).build().getAnnotation();
        validator.initialize(constraintAnnotation);

        var courtDto = createCourtDto(1L, 4);
        var reservationCreateDto = createReservationCreateDto(courtDto, time(12, 0), time(13, 15));

        when(reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15)))
                .thenReturn(true);

        assertThat(validator.isValid(reservationCreateDto, null)).isTrue();
    }

    @Test
    public void isValid_givenUnavailableReservation_returnsFalse() {
        var validator = new ReservationDateAvailableValidator(reservationRepository);
        var constraintAnnotation = new AnnotationDescriptor.Builder<>(ReservationDateAvailable.class).build().getAnnotation();
        validator.initialize(constraintAnnotation);

        var courtDto = createCourtDto(1L, 4);
        var reservationCreateDto = createReservationCreateDto(courtDto, time(12, 0), time(13, 15));

        when(reservationRepository.isDateRangeAvailable(4, time(12, 0), time(13, 15)))
                .thenReturn(false);

        assertThat(validator.isValid(reservationCreateDto, null)).isFalse();
    }

    private LocalDateTime time(int hour, int minute) {
        return LocalDateTime.of(2022, 10, 11, hour, minute);
    }

}
