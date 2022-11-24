package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.exceptions.ServiceException;
import cz.inqool.tennis_club_reservation_system.model.Role;
import cz.inqool.tennis_club_reservation_system.repository.RoleRepositoryImpl;
import cz.inqool.tennis_club_reservation_system.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.RoleDto;
import cz.inqool.tennis_club_reservation_system.model.factory.RoleFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoleServiceTest {

    @MockBean
    private RoleRepositoryImpl roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    public void saveRole_givenValidRole_saves() {
        var role = RoleFactory.createRole(2L, "MODERATOR");
        var expected = new RoleDto(2L, "MODERATOR");

        when(roleRepository.save(any(Role.class)))
                .thenReturn(role);

        var createDto = new RoleCreateDto("MODERATOR");
        var actual = roleService.save(createDto);

        verify(roleRepository).save(any(Role.class));
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    public void editRole_givenMissingRole_throws() {
        var roleDto = RoleFactory.createRoleDto(999L, "USER");

        when(roleRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> roleService.edit(roleDto))
                .withMessage("Role with id 999 not found");
    }

    @Test
    public void editRole_givenValidRole_updates() {
        var roleEditDto = RoleFactory.createRoleDto(2L, "USER");
        var role = RoleFactory.createRole(2L, "ADMIN");
        var roleDto = RoleFactory.createRoleDto(2L, "ADMIN");

        when(roleRepository.findById(any()))
                .thenReturn(Optional.of(role));
        when(roleRepository.update(any()))
                .thenReturn(role);

        var editedUser = roleService.edit(roleEditDto);

        verify(roleRepository).update(any(Role.class));
        assertThat(editedUser).isEqualTo(roleDto);
    }

    @Test
    public void deleteRole_givenInvalidRole_throws() {
        when(roleRepository.findById(eq(1L)))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> roleService.deleteById(1L))
                .withMessage("Role with id 1 not found");
    }

    @Test
    public void deleteRole_givenValidRole_deletes() {
        var role = RoleFactory.createRole(1L, "USER");
        var expected = RoleFactory.createRoleDto(1L, "USER");

        when(roleRepository.findById(eq(1L)))
                .thenReturn(Optional.of(role));

        RoleDto actual = roleService.deleteById(1L);

        verify(roleRepository).softDeleteById(1L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAllRoles_withTwoRoles_returnsAll() {
        var roles = List.of(
                RoleFactory.createRole(1L, "A"),
                RoleFactory.createRole(2L, "B")
        );
        var roleDtos = List.of(
                new RoleDto(1L, "A"),
                new RoleDto(2L, "B")
        );
        var pageable = mock(Pageable.class);

        when(roleRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(roles));

        assertThat(roleService.findAll(pageable))
                .containsExactlyInAnyOrderElementsOf(roleDtos);
    }

}
