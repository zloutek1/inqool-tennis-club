package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.dto.AuthRequestDto;
import cz.inqool.tennis_club_reservation_system.dto.AuthResponseDto;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.exceptions.RefreshTokenExpiredException;
import cz.inqool.tennis_club_reservation_system.service.AuthService;
import cz.inqool.tennis_club_reservation_system.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static cz.inqool.tennis_club_reservation_system.TestUtils.convertToJson;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUserDto;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    // username kayle48
    private final String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrYXlsZTQ4IiwiZXhwIjoxNjM0MDg0MjU3LCJpYXQiOjE2MzQwODA2NTd9.1jZrgQXgpLYGqWBzO5BpXBo8veCfpiUyKWaxg-Ltxy4RDX-w4dNaFOZQSwTI1ykUu46zPOMoGI5ap1xdj3Skug";
    private final String refreshToken = "f22b476d-640e-4113-a6c6-ccce1bab91f1";

    @Test
    public void loginUser_withInvalidCredentials_returnsBadRequest() throws Exception {
        var authRequest = new AuthRequestDto("invalid","pa55");

        when(authService.loginUser(authRequest))
                .thenThrow(new BadCredentialsException("Authentication error: Bad credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .content(convertToJson(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication error: Bad credentials"));
    }

    @Test
    public void loginUser_withExistingCredentials_returnsJwtToken() throws Exception {
        var authRequest = new AuthRequestDto("kayle48", "pa55");
        var userDto = createUserDto(1L, "kayle48");

        when(authService.loginUser(authRequest))
                .thenReturn(new AuthResponseDto(accessToken, refreshToken, userDto));

        mockMvc.perform(post("/api/v1/auth/login")
                        .content(convertToJson(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    @WithMockUser(username="spring")
    public void regenerateAccessToken_givenValidToken_returnsNewJwt() throws Exception {
        var userDto = createUserDto(2L, "kayle48");
        var authResponse = new AuthResponseDto(accessToken, refreshToken, userDto);

        when(refreshTokenService.regenerateAccessToken(refreshToken))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .content(refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.user.id").value(2L));
    }

    @Test
    @WithMockUser(username="spring")
    public void regenerateAccessToken_givenValidAccessToken_returnsNewJwt() throws Exception {
        var userDto = createUserDto(2L, "kayle48");
        var authResponse = new AuthResponseDto(accessToken, refreshToken, userDto);

        when(refreshTokenService.regenerateAccessToken(refreshToken))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken))
                .andExpect(jsonPath("$.user.id").value(2L));
    }


    @Test
    @WithMockUser(username="spring")
    public void regenerateAccessToken_givenInvalidToken_returnsBadRequest() throws Exception {
        when(refreshTokenService.regenerateAccessToken(anyString()))
                .thenThrow(new NotFoundException("Refresh token InvalidToken not found"));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .content("InvalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="spring")
    public void regenerateAccessToken_givenExpiredToken_returnsBadRequest() throws Exception {
        when(refreshTokenService.regenerateAccessToken("ExpiredToken"))
                .thenThrow(new RefreshTokenExpiredException());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .content("ExpiredToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
