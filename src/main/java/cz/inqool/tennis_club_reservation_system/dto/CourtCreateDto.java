package cz.inqool.tennis_club_reservation_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourtCreateDto {

    @NotNull private int courtNumber;

    @NotNull private TerrainDto terrainDto;

}
