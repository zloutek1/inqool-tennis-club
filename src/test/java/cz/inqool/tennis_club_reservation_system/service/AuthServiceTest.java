package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.AuthRequestDto;
import cz.inqool.tennis_club_reservation_system.dto.RefreshTokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUser;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUserDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthService authService;

    // username kayle48
    @SuppressWarnings("FieldCanBeLocal")
    private final String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrYXlsZTQ4IiwiZXhwIjoxNjM0MDg0MjU3LCJpYXQiOjE2MzQwODA2NTd9.1jZrgQXgpLYGqWBzO5BpXBo8veCfpiUyKWaxg-Ltxy4RDX-w4dNaFOZQSwTI1ykUu46zPOMoGI5ap1xdj3Skug";

    @SuppressWarnings("FieldCanBeLocal")
    private final String refreshToken = "f22b476d-640e-4113-a6c6-ccce1bab91f1";

    @Test
    public void loginUser_withInvalidCredentials_returnsBadRequest() {
        var authRequest = new AuthRequestDto("invalid","pa55");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Authentication error: Bad credentials"));

        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> authService.loginUser(authRequest));
    }

    @Test
    public void loginUser_withExistingCredentials_returnsJwtToken() {
        var authRequest = new AuthRequestDto("kayle48", "pa55");

        var user = createUser(1L, "kayle48", "pa55");
        var userDto = createUserDto(1L, "kayle48");
        var refreshTokenDto = new RefreshTokenDto(2L, userDto, refreshToken, Instant.ofEpochMilli(2));
        var authentication = mock(Authentication.class);

        when(authentication.getPrincipal())
                .thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenService.generateAccessToken(userDto))
                .thenReturn(accessToken);
        when(refreshTokenService.generateRefreshTokenForUser(1L))
                .thenReturn(refreshTokenDto);

        var actual = authService.loginUser(authRequest);
        assertThat(actual.getAccessToken()).isEqualTo(accessToken);
        assertThat(actual.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(actual.getUser()).isEqualTo(userDto);
    }
}
