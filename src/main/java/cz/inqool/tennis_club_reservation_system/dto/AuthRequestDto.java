package cz.inqool.tennis_club_reservation_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto implements Serializable {
    @NotBlank
    @Schema(example = "hinevis")
    @NotNull private String username;

    @NotBlank
    @Schema(example = "naih7aiGah")
    @NotNull private String password;
}
