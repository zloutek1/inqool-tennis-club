package cz.inqool.tennis_club_reservation_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrYXlsZTQ4IiwiZXhwIjoxNjM0MDg0MjU3LCJpYXQiOjE2MzQwODA2NTd9.1"
            + "jZrgQXgpLYGqWBzO5BpXBo8veCfpiUyKWaxg-Ltxy4RDX-w4dNaFOZQSwTI1ykUu46zPOMoGI5ap1xdj3Skug")
    @NotNull private String accessToken;

    @Schema(example = "f22b476d-640e-4113-a6c6-ccce1bab91f1")
    @NotNull private String refreshToken;

    @NotNull private UserDto user;
}
