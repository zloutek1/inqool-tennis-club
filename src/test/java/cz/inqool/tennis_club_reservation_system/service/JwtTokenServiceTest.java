package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.FixedTimeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static cz.inqool.tennis_club_reservation_system.model.factory.UserFactory.createUserDto;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FixedTimeConfiguration.class)
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService tokenUtil;

    private final String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMixiaWxsMjIiLCJpc3MiOiJpbnFvb2wuY3oiLCJpYXQiOjE1NTUzMzY4MDAsImV4cCI6MTU1NTMzNjg2MH0.f7N1zKGd0od-GTfzjcIlWsm4XZSGCiSvvzz5-OJnm5KDzDp4VH9iI2yvZ-20-G4p99Rz10Xv9SCk0RCYocI4DQ";

    @Test
    public void generateAccessToken_givenUserDto_returnsJWTToken() {
        var userDto = createUserDto(12L, "bill22");

        var token = tokenUtil.generateAccessToken(userDto);

        assertThat(token).isEqualTo(jwtToken);
    }

    @Test
    public void getUserId_givenValidJWTToken_returnsId() {
        Long id = tokenUtil.getUserId(jwtToken);

        assertThat(id).isEqualTo(12L);
    }

    @Test
    public void getUsername_givenValidJWTToken_returnsUsername() {
        String username = tokenUtil.getUsername(jwtToken);

        assertThat(username).isEqualTo("bill22");
    }

    @Test
    public void getExpirationDate_givenValidJWTToken_returnsExpirationDate() {
        Date expirationDate = tokenUtil.getExpirationDate(jwtToken);
        var expectedDate = LocalDateTime.of(2019, 4, 15, 14, 1, 0).atOffset(ZoneOffset.UTC).toInstant();
        assertThat(expirationDate).isEqualTo(expectedDate);
    }

    @Test
    public void validateToken_givenTokens_checksValidityCorrectly() {
        boolean isValid = tokenUtil.validateToken(jwtToken);

        assertThat(isValid).isTrue();
    }
}
