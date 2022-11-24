package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.RoleDto;
import cz.inqool.tennis_club_reservation_system.model.Role;
import cz.inqool.tennis_club_reservation_system.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class RoleService extends CrudService<Role, Long, RoleDto, RoleCreateDto, RoleDto> {

    public RoleService(RoleRepository roleRepository, BeanMappingService beanMappingService) {
        super(roleRepository, beanMappingService, Role.class, RoleDto.class);
    }
}
