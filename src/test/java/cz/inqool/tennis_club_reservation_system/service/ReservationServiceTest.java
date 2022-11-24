package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.*;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;

    @Test
    void save_givenReservationWithNewUser_savesReservationAndUser() {
        var userCreateDto = createUserCreateDto("bob");
        var reservationCreateDto = createReservationCreateDto(userCreateDto);

        var user = createUser(1L, "bob");
        var savedReservation = createReservation(1L, user);

        var userDto = createUserDto(1L, "bob");
        var expectedReservation = createReservationDto(1L, userDto);

        when(userService.findUserByUsername("bob"))
                .thenReturn(Optional.empty());
        when(userService.save(any()))
                .thenReturn(userDto);
        when(reservationRepository.save(any()))
                .thenReturn(savedReservation);

        var actualReservation = reservationService.save(reservationCreateDto);

        assertThat(actualReservation).isEqualTo(expectedReservation);
        verify(userService).save(userCreateDto);
        verify(userService).addReservation("bob", 1L);
    }

    @Test
    void save_givenReservationWithExistingUser_savesReservation() {
        var userCreateDto = createUserCreateDto("bob");
        var reservationCreateDto = createReservationCreateDto(userCreateDto);

        var user = createUser(1L, "bob");
        var savedReservation = createReservation(1L, user);

        var userDto = createUserDto(1L, "bob");
        var expectedReservation = createReservationDto(1L, userDto);

        when(userService.findUserByUsername("bob"))
                .thenReturn(Optional.of(userDto));
        when(reservationRepository.save(any()))
                .thenReturn(savedReservation);

        var actualReservation = reservationService.save(reservationCreateDto);

        assertThat(actualReservation).isEqualTo(expectedReservation);
        verify(userService, never()).save(userCreateDto);
        verify(userService).addReservation("bob", 1L);
    }
}