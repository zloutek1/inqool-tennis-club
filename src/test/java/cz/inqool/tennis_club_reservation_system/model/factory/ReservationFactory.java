package cz.inqool.tennis_club_reservation_system.model.factory;

import cz.inqool.tennis_club_reservation_system.dto.*;
import cz.inqool.tennis_club_reservation_system.model.Court;
import cz.inqool.tennis_club_reservation_system.model.GameType;
import cz.inqool.tennis_club_reservation_system.model.Reservation;
import cz.inqool.tennis_club_reservation_system.model.User;

import java.time.LocalDateTime;

import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourt;
import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourtDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.*;

public class ReservationFactory {

    private static final GameType defaultGameType = GameType.SINGLES;
    private static final LocalDateTime defaultFromDate = LocalDateTime.of(2022, 7, 2, 11, 20);
    private static final LocalDateTime defaultToDate = LocalDateTime.of(2022, 7, 2, 13, 30);

    private static final Court defaultCourt = createCourt(1L, 4);
    private static final User defaultUser = createUser(6L, "billy");

    private static final CourtDto defaultCourtDto = createCourtDto(1L, 4);
    private static final UserDto defaultUserDto = createUserDto(6L, "billy");
    private static final UserCreateDto defaultUserCreateDto = createUserCreateDto("billy");

    public static Reservation createReservation(Long id, GameType gameType) {
        var reservation = new Reservation(defaultCourt, gameType, defaultFromDate, defaultToDate, defaultUser);
        reservation.setId(id);
        return reservation;
    }

    public static Reservation createReservation(Long id, LocalDateTime fromDate, LocalDateTime toDate) {
        var reservation = new Reservation(defaultCourt, defaultGameType, fromDate, toDate, defaultUser);
        reservation.setId(id);
        return reservation;
    }

    public static ReservationDto createReservationDto(Long id, GameType gameType) {
        return new ReservationDto(id, defaultCourtDto, gameType, defaultFromDate, defaultToDate, defaultUserDto);
    }

    public static ReservationDto createReservationDto(Long id,  LocalDateTime fromDate, LocalDateTime toDate) {
        return new ReservationDto(id, defaultCourtDto, defaultGameType, fromDate, toDate, defaultUserDto);
    }

    public static ReservationCreateDto createReservationCreateDto(GameType gameType) {
        return new ReservationCreateDto(defaultCourtDto, gameType, defaultFromDate, defaultToDate, defaultUserCreateDto);
    }

}
