package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DataPopulationService {

    private final RoleService roleService;
    private final UserService userService;
    private final TerrainService terrainService;
    private final CourtService courtService;

    private final List<TerrainDto> terrains;

    public void populateDatabase() {
        populateRoles();
        populateUsers();
        populateTerrains();
        populateCourts();
    }

    private void populateRoles() {
        roleService.save(new RoleCreateDto("USER"));
        roleService.save(new RoleCreateDto("ADMIN"));
    }

    private void populateUsers() {
        userService.save(new UserCreateDto("+44 333 2222222", "admin", "pass", "pass"));
        userService.addRole("admin", "ADMIN");
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
