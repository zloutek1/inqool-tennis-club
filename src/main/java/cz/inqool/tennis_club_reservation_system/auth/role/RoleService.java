package cz.inqool.tennis_club_reservation_system.auth.role;

import cz.inqool.tennis_club_reservation_system.auth.role.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.auth.role.dto.RoleDto;
import cz.inqool.tennis_club_reservation_system.commons.BeanMappingService;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class RoleService {

    private final BeanMappingService beanMappingService;
    private final RoleRepository roleRepository;

    public RoleService(BeanMappingService beanMappingService, RoleRepository roleRepository) {
        this.beanMappingService = beanMappingService;
        this.roleRepository = roleRepository;
    }

    public RoleDto saveRole(RoleCreateDto roleCreateDto) {
        log.info("Saving new role {} to the database", roleCreateDto.getName());
        Role role = new Role(roleCreateDto.getName());
        role = roleRepository.save(role);
        return beanMappingService.mapTo(role, RoleDto.class);
    }

    public RoleDto editRole(RoleDto roleDto) {
        var roleId = roleDto.getId();
        log.info("Editing role with id {}", roleId);
        tryToFindRole(roleId);

        var role = beanMappingService.mapTo(roleDto, Role.class);
        role = roleRepository.save(role);
        return beanMappingService.mapTo(role, RoleDto.class);
    }

    public RoleDto deleteRole(Long id) {
        log.info("Deleting role with id {}", id);
        var role = tryToFindRole(id);

        roleRepository.deleteById(id);
        return beanMappingService.mapTo(role, RoleDto.class);
    }


    public Page<RoleDto> findAllRoles(Pageable pageable) {
        log.info("Fetching all roles");
        Page<Role> roles = roleRepository.findAll(pageable);
        return beanMappingService.mapTo(roles, RoleDto.class);
    }


    private Role tryToFindRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
    }

}
