package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.model.GameType;
import cz.inqool.tennis_club_reservation_system.service.UserService;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
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
import java.util.Optional;
import java.util.stream.Stream;

import static cz.inqool.tennis_club_reservation_system.TestUtils.convertToJson;
import static cz.inqool.tennis_club_reservation_system.model.factory.ReservationFactory.createReservationDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findAllUsers_withTwoUsers_shouldReturnPaginatedUsers() throws Exception {
        List<UserDto> users = List.of(
                createUserDto(1L, "john22"),
                createUserDto(2L, "bob98")
        );

        when(userService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(users));

        mockMvc.perform(get("/api/v1/user/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].username").value("john22"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findUserById_withValidId_returnsUser() throws Exception {
        var expected = createUserDto(1L, "john22");

        when(userService.findById(1L))
                .thenReturn(Optional.of(expected));

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john22"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findUserById_withInvalidId_returnsNotFound() throws Exception {
        when(userService.findById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findUserByUsername_withValidUsername_returnsUser() throws Exception {
        var expected = createUserDto(1L, "john22");

        when(userService.findUserByUsername("john22"))
                .thenReturn(Optional.of(expected));

        mockMvc.perform(get("/api/v1/user/john22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john22"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findUserByUsername_withInvalidUsername_returnsNotFound() throws Exception {
        when(userService.findUserByUsername("missingUser"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/user/missingUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void newUser_withInvalidForm_returnsBadRequest() throws Exception {
        var createDto = createUserCreateDto("kayle22");
        createDto.setPhoneNumber("hello");

        mockMvc.perform(put("/api/v1/user/new")
                        .content(convertToJson(createDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void newUser_withValidForm_shouldCreateAndReturnNewUser() throws Exception {
        var createDto = createUserCreateDto("kayle22");
        var createdUser = createUserDto(1L, "kayle22");

        when(userService.save(createDto))
                .thenReturn(createdUser);

        mockMvc.perform(put("/api/v1/user/new")
                        .content(convertToJson(createDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("kayle22"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editUser_withValidForm_shouldEditAndReturnEditedUser() throws Exception {
        var userEditDto = createUserEditDto(1L, "kayle23");
        var editedUser = createUserDto(1L, "kayle23");

        when(userService.edit(userEditDto))
                .thenReturn(editedUser);

        mockMvc.perform(put("/api/v1/user/edit")
                        .content(convertToJson(userEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("kayle23"));

        verify(userService).edit(userEditDto);
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void editUser_withInvalidForm_returnsNotFound() throws Exception {
        var userEditDto = createUserEditDto(999L, "kayle22");

        when(userService.edit(userEditDto))
                .thenThrow(new NotFoundException("User with id 999 not found"));

        mockMvc.perform(put("/api/v1/user/edit")
                        .content(convertToJson(userEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteUser_withInvalidId_returnsNotFound() throws Exception {
        when(userService.deleteById(999L))
                .thenThrow(new NotFoundException("User with id 999 not found"));

        mockMvc.perform(delete("/api/v1/user/delete/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void deleteUser_withValidId_shouldDeleteUseAndReturnDeleted() throws Exception {
        var deleteDto = createUserDto(1L, "user-name");

        when(userService.deleteById(1L))
                .thenReturn(deleteDto);

        mockMvc.perform(delete("/api/v1/user/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("user-name"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void addRole_givenValidNames_removesAnimation() throws Exception {
        doNothing().when(userService).addRole("kayle22", "admin");

        mockMvc.perform(post("/api/v1/user/kayle22/role/admin/add"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role with name admin added successfully"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void addRole_givenInvalidUsername_returnsNotFound() throws Exception {
        doThrow(new NotFoundException("User with name missingUser not found"))
                .when(userService).addRole("missingUser", "admin");

        mockMvc.perform(post("/api/v1/user/missingUser/role/admin/add"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void addRole_givenInvalidRoleName_returnsNotFound() throws Exception {
        doThrow(new NotFoundException("Role with name missingRole not found"))
                .when(userService).addRole("kayle22", "missingRole");

        mockMvc.perform(post("/api/v1/user/kayle22/role/missingRole/add"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void removeRole_givenValidNames_removesAnimation() throws Exception {
        doNothing().when(userService).removeRole("kayle22", "admin");

        mockMvc.perform(post("/api/v1/user/kayle22/role/admin/remove"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role with name admin removed successfully"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void removeRole_givenInvalidUsername_returnsNotFound() throws Exception {
        doThrow(new NotFoundException("User with name missingUser not found"))
                .when(userService).removeRole("missingUser", "admin");

        mockMvc.perform(post("/api/v1/user/missingUser/role/admin/remove"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void removeRole_givenInvalidRoleName_returnsNotFound() throws Exception {
        doThrow(new NotFoundException("Role with name missingRole not found"))
                .when(userService).removeRole("kayle22", "missingRole");

        mockMvc.perform(post("/api/v1/user/kayle22/role/missingRole/remove"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findAllUsersReservations_withInvalidUser_returnsNotFound() throws Exception {
        when(userService.findReservations("00456789", false))
                .thenThrow(new NotFoundException("User with phone number 00456789 not found"));

        mockMvc.perform(get("/api/v1/user/00456789/reservations"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findAllCourtReservations_withValidPhoneNumber_returnsReservations() throws Exception {
        var reservations = List.of(
                createReservationDto(2L, GameType.SINGLES),
                createReservationDto(1L, GameType.DOUBLES)
        );

        when(userService.findReservations("00456789", false))
                .thenReturn(reservations);

        mockMvc.perform(get("/api/v1/user/00456789/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].gameType").value("SINGLES"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findAllCourtReservations_withValidPhoneNumberAndFutureSort_returnsReservations() throws Exception {
        var reservations = List.of(
                createReservationDto(2L, GameType.SINGLES),
                createReservationDto(1L, GameType.DOUBLES)
        );

        when(userService.findReservations("00456789", true))
                .thenReturn(reservations);

        mockMvc.perform(get("/api/v1/user/00456789/reservations?future=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].gameType").value("SINGLES"));
    }

    @Test
    @WithMockUser(username="spring", authorities = "ADMIN")
    public void findUserById_givenPostRequest_returnsMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/api/v1/user/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @ParameterizedTest
    @MethodSource("protectedUrls")
    public void endpoint_withoutLogin_returns401(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("protectedUrls")
    @WithMockUser(username="spring", authorities = "USER")
    public void endpoint_withoutPermissions_returns403(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> protectedUrls() throws Exception {
        return Stream.of(
                Arguments.of(get("/api/v1/user/")),
                Arguments.of(get("/api/v1/user/1")),
                Arguments.of(get("/api/v1/user/kyle22")),
                Arguments.of(put("/api/v1/user/new").content(convertToJson(createUserCreateDto("a"))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(put("/api/v1/user/edit").content(convertToJson(createUserEditDto(1L, "A"))).contentType(MediaType.APPLICATION_JSON)),
                Arguments.of(delete("/api/v1/user/delete/1")),
                Arguments.of(post("/api/v1/user/kayle22/role/ADMIN/add")),
                Arguments.of(post("/api/v1/user/kayle22/role/ADMIN/remove"))
        );
    }

}
