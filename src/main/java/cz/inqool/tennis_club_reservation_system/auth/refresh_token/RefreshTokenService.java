package cz.inqool.tennis_club_reservation_system.auth.refresh_token;

import cz.inqool.tennis_club_reservation_system.auth.jwt.JwtTokenUtil;
import cz.inqool.tennis_club_reservation_system.auth.refresh_token.dto.AuthResponseDto;
import cz.inqool.tennis_club_reservation_system.commons.BeanMappingService;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.exceptions.RefreshTokenExpiredException;
import cz.inqool.tennis_club_reservation_system.user.User;
import cz.inqool.tennis_club_reservation_system.user.UserRepository;
import cz.inqool.tennis_club_reservation_system.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@Transactional
public class RefreshTokenService {

    @Value("${jwt.refreshToken.expiration}")
    private Long tokenDuration;

    @PersistenceContext
    private EntityManager entityManager;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final Supplier<UUID> uuidSupplier;
    private final BeanMappingService beanMappingService;
    private final Clock clock;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            JwtTokenUtil jwtTokenUtil,
            Supplier<UUID> uuidSupplier,
            BeanMappingService beanMappingService,
            Clock clock
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.uuidSupplier = uuidSupplier;
        this.beanMappingService = beanMappingService;
        this.clock = clock;
    }

    public RefreshTokenDto generateRefreshTokenForUser(Long userId) {
        log.info("Creating refresh token for user with id {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        RefreshToken refreshToken = new RefreshToken(
                user,
                uuidSupplier.get().toString(),
                Instant.now(clock).plusMillis(tokenDuration)
        );

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);
        entityManager.flush();

        refreshToken = refreshTokenRepository.save(refreshToken);
        return beanMappingService.mapTo(refreshToken, RefreshTokenDto.class);
    }

    public Optional<RefreshTokenDto> findByToken(String token) {
        var foundToken = refreshTokenRepository.findByToken(token);
        return beanMappingService.mapTo(foundToken, RefreshTokenDto.class);
    }

    public boolean verifyTokenExpirationForUser(Long userId) {
        log.info("Verifying token expiration for user with id {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        var token = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Refresh token with user with id " + userId + " not found"));

        if (token.getExpiryDate().compareTo(Instant.now(clock)) < 0) {
            refreshTokenRepository.delete(token);
            return false;
        }
        return true;
    }

    public AuthResponseDto regenerateAccessToken(String refreshToken) {
        var foundToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh token " + refreshToken + " not found"));

        User user = foundToken.getUser();
        if (!verifyTokenExpirationForUser(user.getId())) {
            throw new RefreshTokenExpiredException("Refresh token " + refreshToken + " has expired");
        }

        UserDto userDto = beanMappingService.mapTo(user, UserDto.class);
        String newAccessToken = jwtTokenUtil.generateAccessToken(userDto);
        return new AuthResponseDto(newAccessToken, foundToken.getToken(), userDto);
    }
}
