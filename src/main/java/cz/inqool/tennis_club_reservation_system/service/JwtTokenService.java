package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    @Value("${jwt.accessToken.expiration}")
    private long jwtTokenDuration;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final Clock clock;

    public String generateAccessToken(UserDto userDto) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s,%s", userDto.getId(), userDto.getPhoneNumber(), userDto.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(Date.from(Instant.now(clock)))
                .setExpiration(Date.from(Instant.now(clock).plusMillis(jwtTokenDuration)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserId(String token) {
        Claims claims = parseJWTClaims(token).getBody();
        String id = claims.getSubject().split(",")[0];
        return Long.parseLong(id);
    }

    public String getPhoneNumber(String token) {
        Claims claims = parseJWTClaims(token).getBody();
        return claims.getSubject().split(",")[1];
    }

    public String getUsername(String token) {
        Claims claims = parseJWTClaims(token).getBody();
        return claims.getSubject().split(",")[2];
    }

    public Date getExpirationDate(String token) {
        Claims claims = parseJWTClaims(token).getBody();
        return claims.getExpiration();
    }

    private Jws<Claims> parseJWTClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .setClock(jwtClock(clock))
                .parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            parseJWTClaims(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }

    private io.jsonwebtoken.Clock jwtClock(Clock clock) {
        return () -> Date.from(clock.instant());
    }

}
