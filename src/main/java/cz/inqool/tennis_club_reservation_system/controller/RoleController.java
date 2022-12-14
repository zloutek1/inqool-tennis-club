package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import cz.inqool.tennis_club_reservation_system.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.RoleDto;
import cz.inqool.tennis_club_reservation_system.service.RoleService;
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
@Tag(name = "role")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PutMapping(ApiUris.ROLE_NEW)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDto> newRole(@Valid @RequestBody RoleCreateDto roleDto) {
        RoleDto savedRole = roleService.save(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping(ApiUris.ROLE_EDIT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDto> editRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto editedRole = roleService.edit(roleDto);
        return ResponseEntity.ok(editedRole);
    }

    @DeleteMapping(ApiUris.ROLE_DELETE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleDto> deleteUser(@PathVariable Long id) {
        RoleDto deletedRole = roleService.deleteById(id);
        return ResponseEntity.ok(deletedRole);
    }

    @GetMapping(ApiUris.ROLES)
    @PageableAsQueryParam
    public ResponseEntity<Page<RoleDto>> findAllRoles(@ParameterObject Pageable pageable) {
        Page<RoleDto> roleDtos = roleService.findAll(pageable);
        return ResponseEntity.ok(roleDtos);
    }

}
