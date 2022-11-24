package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import cz.inqool.tennis_club_reservation_system.dto.ReservationDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.model.GameType;
import cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory;
import cz.inqool.tennis_club_reservation_system.service.PriceCalculationService;
import cz.inqool.tennis_club_reservation_system.service.ReservationService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static cz.inqool.tennis_club_reservation_system.TestUtils.convertToJson;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservationCreateDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservationDto;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FixedTimeConfiguration.class)
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private PriceCalculationService priceCalculationService;

    @Test
    @WithMockUser(username="spring")
    public void findAllReservations_withTwoReservations_shouldReturnPaginatedReservations() throws Exception {
        List<ReservationDto> reservations = List.of(
                createReservationDto(1L, GameType.SINGLES),
                createReservationDto(2L, GameType.DOUBLES)
        );

        when(reservationService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(reservations));

        mockMvc.perform(get("/api/v1/reservation/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].gameType").value("SINGLES"));
    }

    @Test
    @WithMockUser(username="spring")
    public void newReservation_withValidForm_shouldCreateAndReturnNewReservation() throws Exception {
        var createDto = ReservationFactory.createReservationCreateDto(GameType.SINGLES);
        var createdReservationDto = createReservationDto(1L, GameType.SINGLES);

        when(reservationService.save(createDto))
                .thenReturn(createdReservationDto);
        when(priceCalculationService.calculatePrice(createdReservationDto))
                .thenReturn(BigDecimal.valueOf(13.22));

        mockMvc.perform(put("/api/v1/reservation/new")
                        .content(convertToJson(createDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(13.22));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editReservation_withValidForm_shouldEditAndReturnEditedReservation() throws Exception {
        var reservationDto = createReservationDto(1L, GameType.SINGLES);
        var editedReservation = createReservationDto(1L, GameType.SINGLES);

        when(reservationService.edit(reservationDto))
                .thenReturn(editedReservation);

        mockMvc.perform(put("/api/v1/reservation/edit")
                        .content(convertToJson(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.gameType").value("SINGLES"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editReservation_withInvalidForm_returnsNotFound() throws Exception {
        var reservationDto = createReservationDto(999L, GameType.DOUBLES);

        when(reservationService.edit(reservationDto))
                .thenThrow(new NotFoundException("Reservation with id 999 not found"));

        mockMvc.perform(put("/api/v1/reservation/edit")
                        .content(convertToJson(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteReservation_withInvalidId_returnsNotFound() throws Exception {
        when(reservationService.deleteById(999L))
                .thenThrow(new NotFoundException("Reservation with id 999 not found"));

        mockMvc.perform(delete("/api/v1/reservation/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteReservation_withValidId_shouldDeleteUseAndReturnDeleted() throws Exception {
        var deleteDto = createReservationDto(1L, GameType.DOUBLES);

        when(reservationService.deleteById(1L))
                .thenReturn(deleteDto);

        mockMvc.perform(delete("/api/v1/reservation/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.gameType").value("DOUBLES"));
    }

    @ParameterizedTest
    @MethodSource("protectedUrls")
    public void endpoint_withoutLogin_returns401(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    private static Stream<Arguments> protectedUrls() throws Exception {
        return Stream.of(
                Arguments.of(get("/api/v1/reservation/")),
                Arguments.of(put("/api/v1/reservation/new").content(convertToJson(createReservationCreateDto(GameType.SINGLES))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(put("/api/v1/reservation/edit").content(convertToJson(createReservationDto(1L, GameType.DOUBLES))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(delete("/api/v1/reservation/delete/1"))
        );
    }

    @ParameterizedTest
    @MethodSource("authorisedUrls")
    @WithMockUser(username="spring", authorities = "USER")
    public void endpoint_withoutPermissions_returns403(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> authorisedUrls() throws Exception {
        return Stream.of(
                Arguments.of(put("/api/v1/reservation/edit").content(convertToJson(createReservationDto(1L, GameType.DOUBLES))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(delete("/api/v1/reservation/delete/1"))
        );
    }
}
