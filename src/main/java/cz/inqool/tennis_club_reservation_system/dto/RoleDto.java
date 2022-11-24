package cz.inqool.tennis_club_reservation_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements EntityDto<Long> {

    @NotNull private Long id;

    @Schema(example = "USER")
    @NotNull private String name;

}
