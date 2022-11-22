package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.RoleCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataPopulationService {

    private final RoleService roleService;

    public void populateDatabase() {
        populateRoles();
    }

    private void populateRoles() {
        roleService.saveRole(new RoleCreateDto("USER"));
        roleService.saveRole(new RoleCreateDto("ADMIN"));
    }

}
