package cz.inqool.tennis_club_reservation_system.controller;

import cz.inqool.tennis_club_reservation_system.configs.ApiUris;
import cz.inqool.tennis_club_reservation_system.dto.AuthRequestDto;
import cz.inqool.tennis_club_reservation_system.dto.AuthResponseDto;
import cz.inqool.tennis_club_reservation_system.service.AuthService;
import cz.inqool.tennis_club_reservation_system.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "auth")
@RequestMapping(ApiUris.ROOT_URI)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(ApiUris.AUTH_LOGIN)
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody AuthRequestDto authRequest) {
        var response = authService.loginUser(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiUris.AUTH_REFRESH)
    public ResponseEntity<AuthResponseDto> regenerateAccessToken(@RequestBody String refreshToken) {
        AuthResponseDto authResponseDto = refreshTokenService.regenerateAccessToken(refreshToken);
        return ResponseEntity.ok(authResponseDto);
    }

}
