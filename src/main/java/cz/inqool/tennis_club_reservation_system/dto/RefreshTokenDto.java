package cz.inqool.tennis_club_reservation_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    @NotNull private Long id;

    @NotNull private UserDto user;

    @Schema(example = "f22b476d-640e-4113-a6c6-ccce1bab91f1")
    @NotNull private String token;

    @Future
    @NotNull private Instant expiryDate;

}
