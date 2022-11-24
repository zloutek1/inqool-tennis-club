package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.model.User;
import cz.inqool.tennis_club_reservation_system.repository.ReservationRepositoryImpl;
import cz.inqool.tennis_club_reservation_system.repository.UserRepositoryImpl;
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
    private UserRepositoryImpl userRepository;

    @MockBean
    private ReservationRepositoryImpl reservationRepository;

    @Autowired
    private ReservationService reservationService;

    private final User user = createUser(1L, "bob");
    private final UserCreateDto userCreateDto = createUserCreateDto("bob");
    private final UserDto userDto = createUserDto(1L, "bob");

    @Test
    void save_givenReservationWithNewUser_savesReservationAndUser() {
        var reservationCreateDto = createReservationCreateDto(userCreateDto);
        var savedReservation = createReservation(1L, user);
        var expectedReservation = createReservationDto(1L, userDto);
        var mockUser = mock(User.class);

        when(userRepository.findByUsername("bob"))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .thenReturn(mockUser);
        when(reservationRepository.save(any()))
                .thenReturn(savedReservation);

        var actualReservation = reservationService.save(reservationCreateDto);

        assertThat(actualReservation).isEqualTo(expectedReservation);
        verify(userRepository).save(user);
        verify(mockUser).addReservation(savedReservation);
    }

    @Test
    void save_givenReservationWithExistingUser_savesReservation() {
        var reservationCreateDto = createReservationCreateDto(userCreateDto);
        var savedReservation = createReservation(1L, user);
        var expectedReservation = createReservationDto(1L, userDto);
        var mockUser= mock(User.class);

        when(userRepository.findByUsername("bob"))
                .thenReturn(Optional.of(mockUser));
        when(reservationRepository.save(any()))
                .thenReturn(savedReservation);

        var actualReservation = reservationService.save(reservationCreateDto);

        assertThat(actualReservation).isEqualTo(expectedReservation);
        verify(userRepository, never()).save(user);
        verify(mockUser).addReservation(savedReservation);
    }
}