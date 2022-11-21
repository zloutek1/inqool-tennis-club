package cz.inqool.tennis_club_reservation_system.auth.refresh_token;

import cz.inqool.tennis_club_reservation_system.user.User;

import java.time.Clock;
import java.time.Instant;

import static cz.inqool.tennis_club_reservation_system.user.UserFactory.createUser;
import static cz.inqool.tennis_club_reservation_system.user.UserFactory.createUserDto;

public class RefreshTokenFactory {

    private static final long defaultExpiresInSeconds = 12;

    public static RefreshToken createRefreshToken(Long id, String token, Clock clock) {
        var user = createUser(id + 1);
        var expiresAt = clock.instant().plusSeconds(defaultExpiresInSeconds);
        var refreshToken = new RefreshToken(user, token, expiresAt);
        refreshToken.setId(id);
        return refreshToken;
    }

    public static RefreshToken createRefreshToken(Long id, User user, String token, Instant expiresAt) {
        var refreshToken = new RefreshToken(user, token, expiresAt);
        refreshToken.setId(id);
        return refreshToken;
    }

    public static RefreshTokenDto createRefreshTokenDto(Long id, String token, Clock clock) {
        var expiresAt = clock.instant().plusSeconds(defaultExpiresInSeconds);
        return new RefreshTokenDto(id, createUserDto(id + 1), token, expiresAt);
    }

}
