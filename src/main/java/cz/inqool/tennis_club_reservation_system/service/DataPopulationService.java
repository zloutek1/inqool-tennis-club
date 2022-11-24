package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.CourtCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataPopulationService {

    private final RoleService roleService;
    private final TerrainService terrainService;
    private final CourtService courtService;

    private final List<TerrainDto> terrains;

    public void populateDatabase() {
        populateRoles();
        populateTerrains();
        populateCourts();
    }

    private void populateRoles() {
        roleService.save(new RoleCreateDto("USER"));
        roleService.save(new RoleCreateDto("ADMIN"));
    }


    private void populateTerrains() {
        terrains.add(terrainService.save(new TerrainCreateDto("dry", BigDecimal.valueOf(2.2))));
        terrains.add(terrainService.save(new TerrainCreateDto("wet", BigDecimal.valueOf(9.3))));
    }

    private void populateCourts() {
        courtService.save(new CourtCreateDto(1, terrains.get(0)));
        courtService.save(new CourtCreateDto(2, terrains.get(0)));
        courtService.save(new CourtCreateDto(6, terrains.get(1)));
        courtService.save(new CourtCreateDto(8, terrains.get(1)));
    }

}
