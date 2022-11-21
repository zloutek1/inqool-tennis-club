package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.model.Role;
import cz.inqool.tennis_club_reservation_system.model.User;
import cz.inqool.tennis_club_reservation_system.repository.RoleRepository;
import cz.inqool.tennis_club_reservation_system.repository.UserRepository;
import cz.inqool.tennis_club_reservation_system.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.dto.UserEditDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BeanMappingService beanMappingService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BeanMappingService beanMappingService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.beanMappingService = beanMappingService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto saveUser(UserCreateDto userCreateDto) {
        log.info("Saving new user {} to the database", userCreateDto);
        User user = beanMappingService.mapTo(userCreateDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    public UserDto editUser(UserEditDto userEditDto) {
        var userId = userEditDto.getId();
        log.info("Editing user with id {}", userId);
        tryToFindUser(userId);

        var user = beanMappingService.mapTo(userEditDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    public UserDto deleteUser(Long id) {
        log.info("Deleting user with id {}", id);
        var user = tryToFindUser(id);

        userRepository.deleteById(id);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    public Page<UserDto> findAllUsers(Pageable pageable) {
        log.info("Fetching all users");
        Page<User> users = userRepository.findAll(pageable);
        return beanMappingService.mapTo(users, UserDto.class);
    }

    public Optional<UserDto> findUserById(Long id) {
        log.info("Fetching user with id {}", id);
        Optional<User> user = userRepository.findById(id);
        return beanMappingService.mapTo(user, UserDto.class);
    }

    public Optional<UserDto> findUserByUsername(String username) {
        log.info("Fetching user {}", username);
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
        log.info("Adding role {} to user {}", role.getName(), user.getFullName());
        user.addRole(role);
    }

    public void removeRole(String username, String roleName) {
        User user = tryToFindUser(username);
        Role role = tryToFindRole(roleName);
        log.info("Removing role {} to user {}", role.getName(), user.getFullName());
        user.removeRole(role);
    }

    private User tryToFindUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }

    private User tryToFindUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }

    private Role tryToFindRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role with name " + roleName + " not found"));
    }

}
