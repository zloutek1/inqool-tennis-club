package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.service.UserService;
import cz.inqool.tennis_club_reservation_system.dto.UserCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.dto.UserEditDto;
import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "user")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private final UserService userService;

    @PutMapping(ApiUris.USER_NEW)
    public ResponseEntity<UserDto> newUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserDto savedUser = userService.save(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping(ApiUris.USER_EDIT)
    public ResponseEntity<UserDto> editUser(@Valid @RequestBody UserEditDto userEditDto) {
        UserDto editedUser = userService.edit(userEditDto);
        return ResponseEntity.ok(editedUser);
    }

    @DeleteMapping(ApiUris.USER_DELETE)
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        UserDto deletedUser = userService.deleteById(id);
        return ResponseEntity.ok(deletedUser);
    }

    @GetMapping(ApiUris.USERS)
    @PageableAsQueryParam
    public ResponseEntity<Page<UserDto>> findAllUsers(@ParameterObject Pageable pageable) {
        Page<UserDto> userDtos = userService.findAll(pageable);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping(ApiUris.USER)
    public ResponseEntity<UserDto> findUser(@PathVariable("id_or_username") String param) {
        try {
            var id = Long.parseLong(param);
            return findUserById(id);
        } catch (NumberFormatException ex) {
            return findUserByUsername(param);
        }
    }

    private ResponseEntity<UserDto> findUserById(Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<UserDto> findUserByUsername(String username) {
        return userService.findUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiUris.USER_ROLE_ADD)
    public ResponseEntity<String> addRole(@PathVariable String username, @PathVariable String roleName) {
        userService.addRole(username, roleName);
        return ResponseEntity.ok("Role with name " + roleName + " added successfully");
    }

    @PostMapping(ApiUris.USER_ROLE_REMOVE)
    public ResponseEntity<String> removeRole(@PathVariable String username, @PathVariable String roleName) {
        userService.removeRole(username, roleName);
        return ResponseEntity.ok("Role with name " + roleName + " removed successfully");
    }

}
