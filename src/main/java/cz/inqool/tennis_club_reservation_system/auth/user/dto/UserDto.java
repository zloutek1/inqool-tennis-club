package cz.inqool.tennis_club_reservation_system.auth.user.dto;

import cz.inqool.tennis_club_reservation_system.auth.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull private Long id;

    @Schema(example = "Theodore J. Baldwin")
    @NotNull private String fullName;

    @Schema(example = "hinevis")
    @NotNull private String username;

    @NotNull private Collection<RoleDto> roles;
}
