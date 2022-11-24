package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Terrain;
import org.springframework.stereotype.Repository;

import java.time.Clock;

@Repository
public class TerrainRepository extends CrudRepositoryImpl<Terrain, Long> {
    public TerrainRepository(Clock clock) {
        super(clock, Terrain.class);
    }
}
