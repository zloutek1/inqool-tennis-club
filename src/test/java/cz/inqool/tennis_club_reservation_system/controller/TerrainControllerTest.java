package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.dto.TerrainDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.model.factory.TerrainFactory;
import cz.inqool.tennis_club_reservation_system.service.TerrainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;
import java.util.stream.Stream;

import static cz.inqool.tennis_club_reservation_system.TestUtils.convertToJson;
import static cz.inqool.tennis_club_reservation_system.model.factory.TerrainFactory.createTerrainCreateDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.TerrainFactory.createTerrainDto;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TerrainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerrainService terrainService;

    @Test
    @WithMockUser(username="spring")
    public void findAllTerrains_withTwoTerrains_shouldReturnPaginatedTerrains() throws Exception {
        List<TerrainDto> terrains = List.of(
                createTerrainDto(1L, "wet"),
                createTerrainDto(2L, "dry")
        );

        when(terrainService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(terrains));

        mockMvc.perform(get("/api/v1/terrain/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].type").value("wet"));
    }

    @Test
    @WithMockUser(username="spring")
    public void newTerrain_withValidForm_shouldCreateAndReturnNewTerrain() throws Exception {
        var createDto = TerrainFactory.createTerrainCreateDto("wet");
        var createdTerrainDto = createTerrainDto(1L, "wet");

        when(terrainService.save(createDto))
                .thenReturn(createdTerrainDto);

        mockMvc.perform(put("/api/v1/terrain/new")
                        .content(convertToJson(createDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("wet"));
    }

    @Test
    @WithMockUser(username="spring")
    public void editTerrain_withValidForm_shouldEditAndReturnEditedTerrain() throws Exception {
        var terrainDto = createTerrainDto(1L, "wet");
        var editedTerrain = createTerrainDto(1L, "wet");

        when(terrainService.edit(terrainDto))
                .thenReturn(editedTerrain);

        mockMvc.perform(put("/api/v1/terrain/edit")
                        .content(convertToJson(terrainDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("wet"));
    }

    @Test
    @WithMockUser(username="spring")
    public void editTerrain_withInvalidForm_returnsNotFound() throws Exception {
        var terrainDto = createTerrainDto(999L, "wet");

        when(terrainService.edit(terrainDto))
                .thenThrow(new NotFoundException("Terrain with id 999 not found"));

        mockMvc.perform(put("/api/v1/terrain/edit")
                        .content(convertToJson(terrainDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring")
    public void deleteTerrain_withInvalidId_returnsNotFound() throws Exception {
        when(terrainService.deleteById(999L))
                .thenThrow(new NotFoundException("Terrain with id 999 not found"));

        mockMvc.perform(delete("/api/v1/terrain/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring")
    public void deleteTerrain_withValidId_shouldDeleteUseAndReturnDeleted() throws Exception {
        var deleteDto = createTerrainDto(1L, "wet");

        when(terrainService.deleteById(1L))
                .thenReturn(deleteDto);

        mockMvc.perform(delete("/api/v1/terrain/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("wet"));
    }

    @ParameterizedTest
    @MethodSource("protectedUrls")
    public void endpoint_withoutLogin_returns401(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> protectedUrls() throws Exception {
        return Stream.of(
                Arguments.of(get("/api/v1/terrain/")),
                Arguments.of(put("/api/v1/terrain/new").content(convertToJson(createTerrainCreateDto("a"))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(put("/api/v1/terrain/edit").content(convertToJson(createTerrainDto(1L, "A"))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(delete("/api/v1/terrain/delete/1"))
        );
    }

}
