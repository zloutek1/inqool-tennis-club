package cz.inqool.tennis_club_reservation_system.dto;

import cz.inqool.tennis_club_reservation_system.validator.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @PhoneNumber
    @Schema(example = "+52 3923 169 322")
    @NotNull private String phoneNumber;

    @NotBlank
    @Schema(example = "hinevis")
    @NotNull private String username;

    @Schema(example = "naih7aiGah")
    @NotNull private String password;

    @Schema(example = "naih7aiGah")
    @NotNull private String confirmPassword;
}
