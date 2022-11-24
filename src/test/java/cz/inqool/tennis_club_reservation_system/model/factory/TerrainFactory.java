package cz.inqool.tennis_club_reservation_system.model.factory;

import cz.inqool.tennis_club_reservation_system.dto.TerrainCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import cz.inqool.tennis_club_reservation_system.model.Terrain;

import java.math.BigDecimal;

public class TerrainFactory {

    private static final BigDecimal defaultPrice = BigDecimal.valueOf(2.00);

    public static Terrain createTerrain(Long id, String terrainType) {
        var terrain = new Terrain(terrainType, defaultPrice);
        terrain.setId(id);
        return terrain;
    }

    public static Terrain createTerrain(Long id, String terrainType, BigDecimal price) {
        var terrain = new Terrain(terrainType, price);
        terrain.setId(id);
        return terrain;
    }

    public static TerrainDto createTerrainDto(Long id, String terrainType) {
        return new TerrainDto(id, terrainType, defaultPrice);
    }

    public static TerrainDto createTerrainDto(Long id, String terrainType, BigDecimal price) {
        return new TerrainDto(id, terrainType, price);
    }

    public static TerrainCreateDto createTerrainCreateDto(String terrainType) {
        return new TerrainCreateDto(terrainType, defaultPrice);
    }

}
