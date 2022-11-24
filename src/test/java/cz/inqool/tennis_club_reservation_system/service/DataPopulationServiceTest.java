package cz.inqool.tennis_club_reservation_system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DataPopulationServiceTest {

    @MockBean
    private RoleService roleService;

    @MockBean
    private TerrainService terrainService;

    @MockBean
    private CourtService courtService;

    @Autowired
    private DataPopulationService dataPopulationService;

    @Test
    void populateDatabase() {
        dataPopulationService.populateDatabase();

        verify(roleService, times(2)).save(any());
        verify(terrainService, times(2)).save(any());
        verify(courtService, times(4)).save(any());
    }
}