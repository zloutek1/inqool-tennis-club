package cz.inqool.tennis_club_reservation_system.model.factory;

import cz.inqool.tennis_club_reservation_system.model.Role;
import cz.inqool.tennis_club_reservation_system.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.RoleDto;

public class RoleFactory {

    public static Role createRole(Long id, String roleName) {
        var role = new Role(roleName);
        role.setId(id);
        return role;
    }

    public static RoleDto createRoleDto(Long id, String roleName) {
        return new RoleDto(id, roleName);
    }


    public static RoleCreateDto createRoleCreateDto(String roleName) {
        return new RoleCreateDto(roleName);
    }

}
