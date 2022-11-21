package cz.inqool.tennis_club_reservation_system.auth.role;

import cz.inqool.tennis_club_reservation_system.auth.role.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.auth.role.dto.RoleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static cz.inqool.tennis_club_reservation_system.auth.role.RoleFactory.createRole;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    public void saveRole_givenValidRole_saves() {
        var role = createRole(2L, "MODERATOR");
        var expected = new RoleDto(2L, "MODERATOR");

        when(roleRepository.save(any(Role.class)))
                .thenReturn(role);

        var createDto = new RoleCreateDto("MODERATOR");
        var actual = roleService.saveRole(createDto);

        verify(roleRepository).save(any(Role.class));
        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    public void findAllRoles_withTwoRoles_returnsAll() {
        var roles = List.of(
                createRole(1L, "A"),
                createRole(2L, "B")
        );
        var roleDtos = List.of(
                new RoleDto(1L, "A"),
                new RoleDto(2L, "B")
        );
        var pageable = mock(Pageable.class);

        when(roleRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(roles));

        assertThat(roleService.findAllRoles(pageable))
                .containsExactlyInAnyOrderElementsOf(roleDtos);
    }

}
