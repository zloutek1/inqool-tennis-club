package cz.inqool.tennis_club_reservation_system.repository;

import cz.inqool.tennis_club_reservation_system.model.Terrain;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.Clock;

@Repository
public class TerrainRepository extends CrudRepositoryImpl<Terrain, Long> {
    public TerrainRepository(EntityManager entityManager, Clock clock) {
        super(entityManager, clock, Terrain.class);
    }
}
