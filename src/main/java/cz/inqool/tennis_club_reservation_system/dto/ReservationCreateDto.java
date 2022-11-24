package cz.inqool.tennis_club_reservation_system.dto;

import cz.inqool.tennis_club_reservation_system.model.GameType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReservationCreateDto {

    @NotNull
    private Long courtId;

    @NotNull
    private GameType gameType;

    @NotNull
    private UserShortDto user;

}
