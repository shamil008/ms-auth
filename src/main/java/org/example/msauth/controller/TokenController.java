package org.example.msauth.controller;

import lombok.RequiredArgsConstructor;
import org.example.msauth.model.dto.AuthResponse;
import org.example.msauth.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.msauth.model.constants.HeaderConstants.USER_ID;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/token")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestHeader(AUTHORIZATION) String refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyToken(@RequestHeader(AUTHORIZATION) String accessToken) {
        var userId = tokenService.verifyToken(accessToken).getUserId();
        return noContent().
                header(USER_ID, userId)
                .build();
    }
}
