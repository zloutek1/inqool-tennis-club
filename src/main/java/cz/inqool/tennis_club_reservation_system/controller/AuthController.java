package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.service.JwtTokenService;
import cz.inqool.tennis_club_reservation_system.dto.RefreshTokenDto;
import cz.inqool.tennis_club_reservation_system.service.RefreshTokenService;
import cz.inqool.tennis_club_reservation_system.dto.AuthRequestDto;
import cz.inqool.tennis_club_reservation_system.dto.AuthResponseDto;
import cz.inqool.tennis_club_reservation_system.service.BeanMappingService;
import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import cz.inqool.tennis_club_reservation_system.model.User;
import cz.inqool.tennis_club_reservation_system.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "auth")
@RequestMapping(ApiUris.ROOT_URI)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final BeanMappingService beanMappingService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService,
            RefreshTokenService refreshTokenService,
            BeanMappingService beanMappingService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
        this.beanMappingService = beanMappingService;
    }

    @PostMapping(ApiUris.AUTH_LOGIN)
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody AuthRequestDto authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        var user = (User) authenticate.getPrincipal();
        var userDto = beanMappingService.mapTo(user, UserDto.class);

        var accessToken = jwtTokenService.generateAccessToken(userDto);
        RefreshTokenDto refreshToken = refreshTokenService.generateRefreshTokenForUser(userDto.getId());
        var response = new AuthResponseDto(accessToken, refreshToken.getToken(), userDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiUris.AUTH_REFRESH)
    public ResponseEntity<AuthResponseDto> regenerateAccessToken(@RequestBody String refreshToken) {
        AuthResponseDto authResponseDto = refreshTokenService.regenerateAccessToken(refreshToken);
        return ResponseEntity.ok(authResponseDto);
    }

}
