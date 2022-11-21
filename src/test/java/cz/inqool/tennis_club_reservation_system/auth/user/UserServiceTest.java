package cz.inqool.tennis_club_reservation_system.auth.user;

import cz.inqool.tennis_club_reservation_system.auth.role.Role;
import cz.inqool.tennis_club_reservation_system.auth.role.RoleRepository;
import cz.inqool.tennis_club_reservation_system.auth.user.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static cz.inqool.tennis_club_reservation_system.auth.user.UserFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> captor;

    @Test
    public void saveUser_givenValidUser_saves() {
        var userCreateDto = createUserCreateDto("karl51", "myPa55w0rd");
        var user = createUser(null, "karl51", "hashedPassword2");
        var savedUser = createUser(2L, "karl51", "hashedPassword2");
        var userDto = createUserDto(2L, "karl51");

        when(userRepository.save(user))
                .thenReturn(savedUser);
        when(passwordEncoder.encode(any()))
                .thenReturn("hashedPassword2");

        var actual = userService.saveUser(userCreateDto);

        verify(userRepository).save(captor.capture());
        verify(passwordEncoder).encode(any());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue()).isEqualTo(user);
        assertThat(actual).isEqualTo(userDto);
    }

    @Test
    public void editUser_givenMissingUser_throws() {
        var userEditDto = createUserEditDto(999L, "karl555");

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userService.editUser(userEditDto))
                .withMessage("User with id 999 not found");
    }

    @Test
    public void editUser_givenValidUser_updates() {
        var userEditDto = createUserEditDto(2L, "karl555");
        var user = createUser(2L, "hashedPassword2");
        var userDto = createUserDto(2L, "hashedPassword2");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(user);
        when(passwordEncoder.encode(any()))
                .thenReturn("hashedPassword2");

        var editedUser = userService.editUser(userEditDto);

        verify(userRepository).save(any(User.class));
        assertThat(editedUser).isEqualTo(userDto);
    }

    @Test
    public void deleteUser_givenInvalidUser_throws() {
        when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userService.deleteUser(1L))
                .withMessage("User with id 1 not found");
    }

    @Test
    public void deleteUser_givenUser_deletes() {
        var user = createUser(1L, "demo");
        var expected = createUserDto(1L, "demo");

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        UserDto actual = userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAllUsers_withTwoUsers_returnsAll() {
        var users = List.of(createUser(1L, "a1"), createUser(2L, "b1"));
        var userDtos = List.of(createUserDto(1L, "a1"), createUserDto(2L, "b1"));
        var pageable = mock(Pageable.class);

        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(users));

        var actual = userService.findAllUsers(pageable);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(userDtos);
    }

    @Test
    public void findUserById_givenExistingId_returns() {
        var user = createUser(2L, "karl51");
        var userDto = createUserDto(2L, "karl51");

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        assertThat(userService.findUserById(2L))
                .contains(userDto);
    }

    @Test
    public void findUserById_givenNonexistentId_returns() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThat(userService.findUserById(999L))
                .isEmpty();
    }

    @Test
    public void findUserByUsername_givenExistingUser_returns() {
        var user = createUser(2L, "karl51");
        var userDto = createUserDto(2L, "karl51");

        when(userRepository.findByUsername("karl51"))
                .thenReturn(Optional.of(user));

        assertThat(userService.findUserByUsername("karl51"))
                .contains(userDto);
    }

    @Test
    public void findUserByUsername_givenNonexistentUsername_returns() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThat(userService.findUserByUsername("missing"))
                .isEmpty();
    }

    @Test
    public void addRole_givenValidRole_adds() {
        var user = mock(User.class);
        var role = mock(Role.class);

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.of(role));

        userService.addRole("bob3", "admin");

        verify(user).addRole(role);
    }

    @Test
    public void addRole_givenInvalidUsername_throws() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userService.addRole("invalid", "admin"))
                .withMessage("User with username invalid not found");
    }

    @Test
    public void addRole_givenInvalidRole_throws() {
        var user = mock(User.class);

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userService.addRole("bob3", "invalid"))
                .withMessage("Role with name invalid not found");
    }

    @Test
    public void removeRole_givenValidRole_removes() {
        var user = mock(User.class);
        var role = mock(Role.class);

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.of(role));

        userService.removeRole("bob3", "admin");

        verify(user).removeRole(role);
    }

    @Test
    public void removeRole_givenInvalidUsername_throws() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userService.removeRole("invalid", "admin"))
                .withMessage("User with username invalid not found");
    }

    @Test
    public void removeRole_givenInvalidRole_throws() {
        var user = mock(User.class);

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userService.removeRole("bob3", "invalid"))
                .withMessage("Role with name invalid not found");
    }

}
