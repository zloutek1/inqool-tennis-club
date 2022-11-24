package cz.inqool.tennis_club_reservation_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtDto implements EntityDto<Long> {

    @NotNull private Long id;

    @NotNull private int number;

    @NotNull private TerrainDto terrainDto;

}
