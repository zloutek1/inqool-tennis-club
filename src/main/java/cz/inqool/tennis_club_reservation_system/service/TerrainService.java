package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.TerrainCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import cz.inqool.tennis_club_reservation_system.model.Terrain;
import cz.inqool.tennis_club_reservation_system.repository.TerrainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TerrainService extends CrudService<Terrain, Long, TerrainDto, TerrainCreateDto, TerrainDto> {


    public TerrainService(TerrainRepository terrainRepository, BeanMappingService beanMappingService) {
        super(terrainRepository, beanMappingService, Terrain.class, TerrainDto.class);
    }
}
