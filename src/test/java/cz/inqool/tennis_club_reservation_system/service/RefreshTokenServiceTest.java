package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import cz.inqool.tennis_club_reservation_system.dto.RefreshTokenDto;
import cz.inqool.tennis_club_reservation_system.exceptions.ServiceException;
import cz.inqool.tennis_club_reservation_system.model.RefreshToken;
import cz.inqool.tennis_club_reservation_system.repository.RefreshTokenRepositoryImpl;
import cz.inqool.tennis_club_reservation_system.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static cz.inqool.tennis_club_reservation_system.model.factory.RefreshTokenFactory.createRefreshToken;
import static cz.inqool.tennis_club_reservation_system.model.factory.RefreshTokenFactory.createRefreshTokenDto;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUser;
import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUserDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = FixedTimeConfiguration.class)
public class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RefreshTokenRepositoryImpl refreshTokenRepository;

    @MockBean
    private UserRepositoryImpl userRepository;

    @MockBean
    private Supplier<UUID> uuidSupplier;

    @Autowired
    private Clock clock;

    final String token = "5211e915-c3e2-4dcb-0776-c7b900f38ab7";

    @Test
    public void generateRefreshTokenForUser_givenValidId_returnsNewToken() {
        var user = createUser(1L);
        Instant now = Instant.now(clock).plusMillis(120000);
        var generatedToken = new RefreshToken(user, token, now);
        var savedRefreshToken = createRefreshToken(1L, user, token, now);
        var generatedTokenDto = new RefreshTokenDto(1L, createUserDto(1L), token, now);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenReturn(savedRefreshToken);
        when(uuidSupplier.get())
                .thenReturn(UUID.fromString(token));

        var actual = refreshTokenService.generateRefreshTokenForUser(1L);

        verify(refreshTokenRepository).save(generatedToken);
        assertThat(actual).isEqualTo(generatedTokenDto);
    }

    @Test
    public void generateRefreshTokenForUser_givenInvalidId_throws() {
        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> refreshTokenService.generateRefreshTokenForUser(999L))
                .withMessage("User with id 999 not found");
    }

    @Test
    public void findByToken_givenExistingToken_returns() {
        var refreshToken = createRefreshToken(1L, "tok1", clock);
        var refreshTokenDto = createRefreshTokenDto(1L, "tok1", clock);

        when(refreshTokenRepository.findByToken("tok1"))
                .thenReturn(Optional.of(refreshToken));

        var actual = refreshTokenService.findByToken("tok1");

        assertThat(actual).contains(refreshTokenDto);
    }

    @Test
    public void findByToken_givenNonExistingToken_returnsEmpty() {
        when(refreshTokenRepository.findByToken("tokMissing"))
                .thenReturn(Optional.empty());

        var actual = refreshTokenService.findByToken("tokMissing");

        assertThat(actual).isEmpty();
    }

    @Test
    public void verifyTokenExpirationForUser_givenValidToken_returnsTrue() {
        var user = createUser(1L);
        var refreshToken = createRefreshToken(1L, user, "tok22", Instant.now(clock).plusSeconds(15));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.of(refreshToken));

        var actual = refreshTokenService.verifyTokenExpirationForUser(1L);

        assertThat(actual).isTrue();
    }

    @Test
    public void verifyTokenExpirationForUser_givenExpiredToken_returnsFalse() {
        var user = createUser(1L);
        var refreshToken = createRefreshToken(1L, user, "tok22", Instant.now(clock).minusSeconds(6));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.of(refreshToken));

        var actual = refreshTokenService.verifyTokenExpirationForUser(1L);

        assertThat(actual).isFalse();
        verify(refreshTokenRepository).softDeleteById(1L);
    }

    @Test
    public void verifyTokenExpirationForUser_givenAboutToExpireToken_returnsTrue() {
        var user = createUser(1L);
        var refreshToken = createRefreshToken(1L, user, "tok22", Instant.now(clock));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.of(refreshToken));

        var actual = refreshTokenService.verifyTokenExpirationForUser(1L);

        assertThat(actual).isTrue();
    }

    @Test
    public void verifyTokenExpirationForUser_givenInvalidUser_throws() {
        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> refreshTokenService.verifyTokenExpirationForUser(2L))
                .withMessage("User with id 2 not found");
    }

    @Test
    public void verifyTokenExpirationForUser_givenMissingToken_throws() {
        var user = createUser(2L);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> refreshTokenService.verifyTokenExpirationForUser(2L))
                .withMessage("Refresh token with user with id 2 not found");
    }

    @Test
    void regenerateAccessToken_givenValidToken_generatesNew() {
        var refreshToken = createRefreshToken(1L, token, clock);
        var user = createUser(2L);

        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.of(refreshToken));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.of(refreshToken));

        var actual = refreshTokenService.regenerateAccessToken(token);
        assertThat(actual.getRefreshToken()).isEqualTo(token);
        assertThat(actual.getAccessToken()).isEqualTo("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyLHRoZUNvb2xHdXk0NCIsImlzcyI6ImlucW9vbC5jeiIsImlhdCI6MTU1NTMzNjgwMCwiZXhwIjoxNTU1MzM2ODYwfQ.2NPYJnD8Eg2CgyVAN0yHrg0qLoTnOR11x2JzCi9cAtwffpPCKcTkoYGEf3TYAGFVCvAlVXoKJBTFegY8cz0rAw");
    }

    @Test
    void regenerateAccessToken_givenInvalidToken_generatesNew() {
        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> refreshTokenService.regenerateAccessToken(token))
                .withMessage("Refresh token 5211e915-c3e2-4dcb-0776-c7b900f38ab7 not found");
    }

    @Test
    void regenerateAccessToken_givenExpiredToken_generatesNew() {
        var user = createUser(2L);
        var refreshToken = createRefreshToken(1L, user, token, Instant.now(clock).minusSeconds(6));

        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.of(refreshToken));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user))
                .thenReturn(Optional.of(refreshToken));

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> refreshTokenService.regenerateAccessToken(token))
                .withMessage("Refresh token 5211e915-c3e2-4dcb-0776-c7b900f38ab7 has expired");
    }






}
