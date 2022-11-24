package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.dto.CourtDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.model.GameType;
import cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory;
import cz.inqool.tennis_club_reservation_system.service.CourtService;
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
import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourtCreateDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.CourtFactory.createCourtDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservationDto;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourtService courtService;

    @Test
    @WithMockUser(username="spring")
    public void findAllCourts_withTwoCourts_shouldReturnPaginatedCourts() throws Exception {
        List<CourtDto> users = List.of(
                createCourtDto(1L, 1),
                createCourtDto(2L, 2)
        );

        when(courtService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(users));

        mockMvc.perform(get("/api/v1/court/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].number").value(1));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void newCourt_withValidForm_shouldCreateAndReturnNewCourt() throws Exception {
        var createDto = CourtFactory.createCourtCreateDto(4);
        var createdCourtDto = createCourtDto(1L, 4);

        when(courtService.save(createDto))
                .thenReturn(createdCourtDto);

        mockMvc.perform(put("/api/v1/court/new")
                        .content(convertToJson(createDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value(4));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editCourt_withValidForm_shouldEditAndReturnEditedCourt() throws Exception {
        var courtDto = createCourtDto(1L, 5);
        var editedCourt = createCourtDto(1L, 5);

        when(courtService.edit(courtDto))
                .thenReturn(editedCourt);

        mockMvc.perform(put("/api/v1/court/edit")
                        .content(convertToJson(courtDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value(5));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editCourt_withInvalidForm_returnsNotFound() throws Exception {
        var courtDto = createCourtDto(999L, 5);

        when(courtService.edit(courtDto))
                .thenThrow(new NotFoundException("Court with id 999 not found"));

        mockMvc.perform(put("/api/v1/court/edit")
                        .content(convertToJson(courtDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteCourt_withInvalidId_returnsNotFound() throws Exception {
        when(courtService.deleteById(999L))
                .thenThrow(new NotFoundException("Court with id 999 not found"));

        mockMvc.perform(delete("/api/v1/court/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteCourt_withValidId_shouldDeleteUseAndReturnDeleted() throws Exception {
        var deleteDto = createCourtDto(1L, 5);

        when(courtService.deleteById(1L))
                .thenReturn(deleteDto);

        mockMvc.perform(delete("/api/v1/court/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value(5));
    }

    @Test
    @WithMockUser(username="spring")
    public void findAllCourtReservations_withInvalidCourtNumber_returnsNotFound() throws Exception {
        when(courtService.findReservations(4))
                .thenThrow(new NotFoundException("Court with number 4 not found"));

        mockMvc.perform(get("/api/v1/court/4/reservations"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring")
    public void findAllCourtReservations_withValidCourtNumber_returnsReservations() throws Exception {
        var reservations = List.of(
                createReservationDto(2L, GameType.SINGLES),
                createReservationDto(1L, GameType.DOUBLES)
        );

        when(courtService.findReservations(4))
                .thenReturn(reservations);

        mockMvc.perform(get("/api/v1/court/4/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].gameType").value("SINGLES"));
    }

    @ParameterizedTest
    @MethodSource("protectedUrls")
    public void endpoint_withoutLogin_returns401(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> protectedUrls() throws Exception {
        return Stream.of(
                Arguments.of(get("/api/v1/court/")),
                Arguments.of(put("/api/v1/court/new").content(convertToJson(createCourtCreateDto(5))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(put("/api/v1/court/edit").content(convertToJson(createCourtDto(1L, 6))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(delete("/api/v1/court/delete/1"))
        );
    }

    @ParameterizedTest
    @MethodSource("authorizedUrls")
    @WithMockUser(username="spring", authorities = "USER")
    public void endpoint_withoutPermissions_returns403(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> authorizedUrls() throws Exception {
        return Stream.of(
                Arguments.of(put("/api/v1/court/new").content(convertToJson(createCourtCreateDto(5))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(put("/api/v1/court/edit").content(convertToJson(createCourtDto(1L, 6))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(delete("/api/v1/court/delete/1"))
        );
    }
}
