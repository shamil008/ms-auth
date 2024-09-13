package org.example.msauth.service;

import lombok.RequiredArgsConstructor;
import org.example.msauth.client.UserClient;
import org.example.msauth.model.dto.AuthResponse;
import org.example.msauth.model.request.AuthRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserClient userClient;
    private final TokenService tokenService;

    public AuthResponse signIn(AuthRequest authRequest) {
        var userResponseDto = userClient.getUserDetails(authRequest.getUsername());
        return tokenService.prepareToken(userResponseDto.id());
    }

}
