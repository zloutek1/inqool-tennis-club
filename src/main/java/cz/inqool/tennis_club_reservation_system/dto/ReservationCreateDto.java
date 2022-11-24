package cz.inqool.tennis_club_reservation_system.dto;

import cz.inqool.tennis_club_reservation_system.model.GameType;
import cz.inqool.tennis_club_reservation_system.validator.ReservationDateAvailable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ReservationDateAvailable
public class ReservationCreateDto {

    @NotNull private CourtDto court;

    @NotNull private GameType gameType;

    @FutureOrPresent
    @NotNull private LocalDateTime fromDate;

    @Future
    @NotNull private LocalDateTime toDate;

    @NotNull private UserCreateDto user;

}
