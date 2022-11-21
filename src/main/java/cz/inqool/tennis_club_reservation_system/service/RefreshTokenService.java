package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.AuthResponseDto;
import cz.inqool.tennis_club_reservation_system.dto.RefreshTokenDto;
import cz.inqool.tennis_club_reservation_system.repository.RefreshTokenRepository;
import cz.inqool.tennis_club_reservation_system.exceptions.NotFoundException;
import cz.inqool.tennis_club_reservation_system.exceptions.RefreshTokenExpiredException;
import cz.inqool.tennis_club_reservation_system.model.RefreshToken;
import cz.inqool.tennis_club_reservation_system.model.User;
import cz.inqool.tennis_club_reservation_system.repository.UserRepository;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
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
    private final JwtTokenService jwtTokenService;
    private final Supplier<UUID> uuidSupplier;
    private final BeanMappingService beanMappingService;
    private final Clock clock;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            JwtTokenService jwtTokenService,
            Supplier<UUID> uuidSupplier,
            BeanMappingService beanMappingService,
            Clock clock
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.uuidSupplier = uuidSupplier;
        this.beanMappingService = beanMappingService;
        this.clock = clock;
    }

    public RefreshTokenDto generateRefreshTokenForUser(Long userId) {
        log.info("Creating refresh token for user with id {}", userId);
        User user = tryToFindUserById(userId);

        RefreshToken refreshToken = new RefreshToken(
                user,
                uuidSupplier.get().toString(),
                Instant.now(clock).plusMillis(tokenDuration)
        );

        refreshTokenRepository.findByUser(user)
                .ifPresent(token -> refreshTokenRepository.deleteById(token.getId()));
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
        var user = tryToFindUserById(userId);
        var token = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Refresh token with user with id " + userId + " not found"));

        if (token.getExpiryDate().compareTo(Instant.now(clock)) < 0) {
            refreshTokenRepository.deleteById(token.getId());
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
        String newAccessToken = jwtTokenService.generateAccessToken(userDto);
        return new AuthResponseDto(newAccessToken, foundToken.getToken(), userDto);
    }

    private User tryToFindUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}
