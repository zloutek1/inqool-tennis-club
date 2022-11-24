package cz.inqool.tennis_club_reservation_system.model.factory;

import cz.inqool.tennis_club_reservation_system.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.dto.UserEditDto;
import cz.inqool.tennis_club_reservation_system.model.Reservation;
import cz.inqool.tennis_club_reservation_system.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static cz.inqool.tennis_club_reservation_system.model.factory.RoleFactory.createRole;

public class UserFactory {

    private static final String defaultPhoneNumber = "202 555 0125";
    private static final String defaultUsername = "kayle22";
    private static final String defaultPassword = "IcK7wDN3hs";

    public static User createUser(Long id) {
        var user = new User(defaultPhoneNumber, defaultUsername, defaultPassword);
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

    public static User createUser(Long id, String username, String[] roleNames) {
        var user = createUser(id, username);

        user.setRoles(new HashSet<>());
        for (int i = 0; i < roleNames.length; ++i) {
            user.addRole(createRole((long) i, roleNames[i]));
        }

        return user;
    }

    public static User createUser(Long id, String phoneNumber, String username, String password) {
        var user = createUser(id, username, password);
        user.setPhoneNumber(phoneNumber);
        return user;
    }

    public static User createUser(Long id, String phoneNumber, String username, Reservation[] reservations) {
        var user = createUser(id, phoneNumber, username);
        user.setReservations(Arrays.asList(reservations));
        return user;
    }

    public static UserDto createUserDto(Long id) {
        return new UserDto(id, defaultPhoneNumber, defaultUsername, List.of());
    }

    public static UserDto createUserDto(Long id, String username) {
        var userDto = createUserDto(id);
        userDto.setUsername(username);
        return userDto;
    }

    public static UserDto createUserDto(Long id, String phoneNumber, String username) {
        var user = createUserDto(id, username);
        user.setPhoneNumber(phoneNumber);
        return user;
    }


    public static UserEditDto createUserEditDto(Long id, String newUsername) {
        return new UserEditDto(id, defaultPhoneNumber, newUsername, defaultPassword, defaultPassword);
    }

    public static UserCreateDto createUserCreateDto(String username) {
        return new UserCreateDto(defaultPhoneNumber, username, defaultPassword, defaultPassword);
    }

    public static UserCreateDto createUserCreateDto(String username, String password) {
        return new UserCreateDto(defaultPhoneNumber, username, password, password);
    }
}
