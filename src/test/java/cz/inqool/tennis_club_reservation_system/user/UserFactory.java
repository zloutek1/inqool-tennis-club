package cz.inqool.tennis_club_reservation_system.user;

import cz.inqool.tennis_club_reservation_system.user.User;
import cz.inqool.tennis_club_reservation_system.user.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.user.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.user.dto.UserEditDto;

import java.util.List;

public class UserFactory {

    private static final String defaultFullName = "John Snow";
    private static final String defaultUsername = "theCoolGuy44";
    private static final String defaultPassword = "IcK7wDN3hs";

    public static User createUser(Long id) {
        var user = new User(defaultFullName, defaultUsername, defaultPassword);
        user.setId(id);
        return user;
    }

    public static User createUser(Long id, String username) {
        var user = createUser(id);
        user.setUsername(username);
        return user;
    }

    public static User createUser(Long id, String username, String password) {
        var user = createUser(id, username);
        user.setPassword(password);
        return user;
    }

    public static UserDto createUserDto(Long id) {
        return new UserDto(id, defaultFullName, defaultUsername, List.of());
    }

    public static UserDto createUserDto(Long id, String username) {
        var userDto = createUserDto(id);
        userDto.setUsername(username);
        return userDto;
    }

    public static UserEditDto createUserEditDto(Long id, String newUsername) {
        return new UserEditDto(id, defaultFullName, newUsername, defaultPassword, defaultPassword);
    }

    public static UserCreateDto createUserCreateDto(String username) {
        return new UserCreateDto(defaultFullName, username, defaultPassword, defaultPassword);
    }

    public static UserCreateDto createUserCreateDto(String username, String password) {
        return new UserCreateDto(defaultFullName, username, password, password);
    }
}
