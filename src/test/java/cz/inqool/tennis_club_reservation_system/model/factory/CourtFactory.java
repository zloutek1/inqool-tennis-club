package cz.inqool.tennis_club_reservation_system.model.factory;

import cz.inqool.tennis_club_reservation_system.dto.CourtCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.CourtDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import cz.inqool.tennis_club_reservation_system.model.Court;
import cz.inqool.tennis_club_reservation_system.model.Reservation;
import cz.inqool.tennis_club_reservation_system.model.Terrain;

import java.util.List;

import static cz.inqool.tennis_club_reservation_system.model.factory.TerrainFactory.createTerrain;
import static cz.inqool.tennis_club_reservation_system.model.factory.TerrainFactory.createTerrainDto;

public class CourtFactory {

    private static final Terrain defaultTerrain = createTerrain(1L, "wet");
    private static final TerrainDto defaultTerrainDto = createTerrainDto(1L, "wet");

    public static Court createCourt(Long id, int courtNumber) {
        var court = new Court(courtNumber, defaultTerrain);
        court.setId(id);
        return court;
    }

    public static Court createCourt(Long id, int courtNumber, List<Reservation> reservations) {
        var court = createCourt(id, courtNumber);
        court.setReservations(reservations);
        return court;
    }

    public static CourtDto createCourtDto(Long id, int courtNumber) {
        return new CourtDto(id, courtNumber, defaultTerrainDto);
    }

    public static CourtDto createCourtDto(Long id, int courtNumber, TerrainDto terrainDto) {
        return new CourtDto(id, courtNumber, terrainDto);
    }

    public static CourtCreateDto createCourtCreateDto(int courtNumber) {
        return new CourtCreateDto(courtNumber, defaultTerrainDto);
    }

}
