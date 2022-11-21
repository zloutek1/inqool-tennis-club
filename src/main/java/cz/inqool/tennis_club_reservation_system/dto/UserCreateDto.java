package cz.inqool.tennis_club_reservation_system.dto;

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
    @NotBlank
    @Schema(example = "Theodore J. Baldwin")
    @NotNull private String fullName;

    @NotBlank
    @Schema(example = "hinevis")
    @NotNull private String username;

    @Schema(example = "naih7aiGah")
    @NotNull private String password;

    @Schema(example = "naih7aiGah")
    @NotNull private String confirmPassword;
}
