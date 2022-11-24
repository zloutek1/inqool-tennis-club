package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.dto.UserEditDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.model.Role;
import cz.inqool.tennis_club_reservation_system.model.User;
import cz.inqool.tennis_club_reservation_system.repository.RoleRepository;
import cz.inqool.tennis_club_reservation_system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserService extends CrudService<User, Long, UserDto, UserCreateDto, UserEditDto> implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public UserService(UserRepository userRepository, BeanMappingService beanMappingService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, Clock clock) {
        super(userRepository, beanMappingService, User.class, UserDto.class);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Override
    public UserDto save(UserCreateDto userCreateDto) {
        log.info("Saving new user {} to the database", userCreateDto);
        User user = beanMappingService.mapTo(userCreateDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    @Override
    public UserDto edit(UserEditDto userEditDto) {
        var userId = userEditDto.getId();
        log.info("Editing User with id {}", userId);
        tryToFindEntity(userId);

        var user = beanMappingService.mapTo(userEditDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    public Optional<UserDto> findUserByUsername(String username) {
        log.info("Fetching User {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return tryToFindUser(username);
        } catch (NotFoundException ex) {
            throw new UsernameNotFoundException(ex.getMessage(), ex);
        }
    }

    public void addRole(String username, String roleName) {
        User user = tryToFindUser(username);
        Role role = tryToFindRole(roleName);
        log.info("Adding Role {} to User {}", role.getName(), user.getUsername());
        user.addRole(role);
    }

    public void removeRole(String username, String roleName) {
        User user = tryToFindUser(username);
        Role role = tryToFindRole(roleName);
        log.info("Removing Role {} to User {}", role.getName(), user.getUsername());
        user.removeRole(role);
    }


    public List<ReservationDto> findReservations(String phoneNumber, boolean future) {
        User user = tryToFindUserByPhoneNumber(phoneNumber);
        log.info("Finding reservations of user {}", user.getUsername());

        var reservations = user.getReservations();

        if (future) {
            var now = LocalDateTime.now(clock);
            reservations = reservations.stream()
                    .filter(res -> res.getFromDate().isAfter(now))
                    .collect(Collectors.toList());
        }

        return beanMappingService.mapTo(reservations, ReservationDto.class);
    }

    private User tryToFindUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }

    private User tryToFindUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User with phone number " + phoneNumber + " not found"));
    }

    private Role tryToFindRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role with name " + roleName + " not found"));
    }
}
