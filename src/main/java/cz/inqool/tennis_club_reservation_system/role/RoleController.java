package cz.inqool.tennis_club_reservation_system.role;

import cz.inqool.tennis_club_reservation_system.role.dto.RoleCreateDto;
import cz.inqool.tennis_club_reservation_system.role.dto.RoleDto;
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
@Tag(name = "role")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @PutMapping(ApiUris.ROLE_NEW)
    public ResponseEntity<RoleDto> newRole(@Valid @RequestBody RoleCreateDto roleDto) {
        RoleDto savedRole = roleService.saveRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping(ApiUris.ROLE_EDIT)
    public ResponseEntity<RoleDto> editRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto editedRole = roleService.editRole(roleDto);
        return ResponseEntity.ok(editedRole);
    }

    @DeleteMapping(ApiUris.ROLE_DELETE)
    public ResponseEntity<RoleDto> deleteUser(@PathVariable Long id) {
        RoleDto deletedRole = roleService.deleteRole(id);
        return ResponseEntity.ok(deletedRole);
    }

    @GetMapping(ApiUris.ROLES)
    @PageableAsQueryParam
    public ResponseEntity<Page<RoleDto>> findALlRoles(@ParameterObject Pageable pageable) {
        Page<RoleDto> roleDtos = roleService.findAllRoles(pageable);
        return ResponseEntity.ok(roleDtos);
    }

}
