package cz.inqool.tennis_club_reservation_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerrainDto implements EntityDto<Long> {

    @NotNull private Long id;

    @Schema(example = "wet")
    @NotNull private String type;

    @Schema(example = "2.00")
    @NotNull private BigDecimal price;

}
