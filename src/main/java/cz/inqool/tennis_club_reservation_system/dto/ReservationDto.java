package cz.inqool.tennis_club_reservation_system.dto;

import cz.inqool.tennis_club_reservation_system.model.GameType;
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
public class ReservationDto implements EntityDto<Long> {

    @NotNull private Long id;

    @NotNull private CourtDto court;

    @NotNull private GameType gameType;

    @FutureOrPresent
    @NotNull private LocalDateTime fromDate;

    @Future
    @NotNull private LocalDateTime toDate;

    @NotNull private UserDto user;

}
