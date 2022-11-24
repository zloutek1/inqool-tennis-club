package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import cz.inqool.tennis_club_reservation_system.dto.TerrainCreateDto;
import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import cz.inqool.tennis_club_reservation_system.service.TerrainService;
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
@Tag(name = "reservation")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
public class TerrainController {

    private final TerrainService terrainService;

    @PutMapping(ApiUris.TERRAIN_NEW)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TerrainDto> newTerrain(@Valid @RequestBody TerrainCreateDto createDto) {
        TerrainDto savedTerrain = terrainService.save(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTerrain);
    }

    @PutMapping(ApiUris.TERRAIN_EDIT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TerrainDto> editTerrain(@Valid @RequestBody TerrainDto editDto) {
        TerrainDto editedTerrain = terrainService.edit(editDto);
        return ResponseEntity.ok(editedTerrain);
    }

    @DeleteMapping(ApiUris.TERRAIN_DELETE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TerrainDto> deleteTerrain(@PathVariable Long id) {
        TerrainDto deletedTerrain = terrainService.deleteById(id);
        return ResponseEntity.ok(deletedTerrain);
    }

    @GetMapping(ApiUris.TERRAINS)
    @PageableAsQueryParam
    public ResponseEntity<Page<TerrainDto>> findAllTerrains(@ParameterObject Pageable pageable) {
        Page<TerrainDto> terrains = terrainService.findAll(pageable);
        return ResponseEntity.ok(terrains);
    }

}
