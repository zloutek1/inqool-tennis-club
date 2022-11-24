package cz.inqool.tennis_club_reservation_system.service;

import cz.inqool.tennis_club_reservation_system.dto.AuthRequestDto;
import cz.inqool.tennis_club_reservation_system.dto.AuthResponseDto;
import cz.inqool.tennis_club_reservation_system.dto.RefreshTokenDto;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import cz.inqool.tennis_club_reservation_system.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final BeanMappingService beanMappingService;

    public AuthResponseDto loginUser(AuthRequestDto authRequest) {
        UserDto userDto = authenticate(authRequest.getUsername(), authRequest.getPassword());

        var accessToken = jwtTokenService.generateAccessToken(userDto);
        RefreshTokenDto refreshToken = refreshTokenService.generateRefreshTokenForUser(userDto.getId());

        return new AuthResponseDto(accessToken, refreshToken.getToken(), userDto);
    }

    private UserDto authenticate(String username, String password) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        var user = (User) authenticate.getPrincipal();
        return beanMappingService.mapTo(user, UserDto.class);
    }

}
