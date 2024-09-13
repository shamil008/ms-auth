package org.example.msauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msauth.configuration.property.TokenExpirationProperties;
import org.example.msauth.exception.AuthException;
import org.example.msauth.model.cache.AuthCacheData;
import org.example.msauth.model.dto.AuthPayloadDto;
import org.example.msauth.model.dto.AuthResponse;
import org.example.msauth.model.jwt.AccessTokenClaimsSet;
import org.example.msauth.model.jwt.RefreshTokenClaimsSet;
import org.example.msauth.util.CacheUtil;
import org.springframework.stereotype.Service;

import static jodd.util.Base64.encodeToString;
import static org.example.msauth.mapper.TokenMapper.TOKEN_MAPPER;
import static org.example.msauth.model.constants.CacheConstants.CACHE_EXPIRE_SECONDS;
import static org.example.msauth.model.enums.ExceptionConstants.*;
import static org.example.msauth.util.CertificateKeyUtil.CERTIFICATE_KEY_UTIL;
import static org.example.msauth.util.JwtUtil.JWT_UTIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenExpirationProperties tokenExpirationProperties;
    private final CacheUtil cacheProvider;

    public AuthResponse prepareToken(String userId) {
        final var refreshTokenExpirationCount = 50;
        return generateToken(userId, refreshTokenExpirationCount);
    }

    public AuthResponse refreshToken(String refreshToken) {
        var refreshTokenClaimsSet = JWT_UTIL.getClaimsFromToken(refreshToken, RefreshTokenClaimsSet.class);
        var refreshTokenExpirationCount = refreshTokenClaimsSet.getCount() - 1;
        var userId = refreshTokenClaimsSet.getUserId();

        try {
            var authCacheData = fetchFromCache(userId);

            if (authCacheData == null) throw new AuthException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);

            var publicKey = CERTIFICATE_KEY_UTIL.getPublicKey(authCacheData.getPublicKey());

            JWT_UTIL.verifySignature(refreshToken, publicKey);

            verifyRefreshToken(refreshTokenClaimsSet);

            return generateToken(userId, refreshTokenExpirationCount);
        } catch (AuthException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    public AuthPayloadDto verifyToken(String accessToken) {
        var userId = JWT_UTIL.getClaimsFromToken(accessToken, AccessTokenClaimsSet.class).getUserId();

        try {
            var authCacheData = fetchFromCache(userId);

            if (authCacheData == null) throw new AuthException(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage(), 406);

            var publicKey = CERTIFICATE_KEY_UTIL.getPublicKey(authCacheData.getPublicKey());

            JWT_UTIL.verifySignature(accessToken, publicKey);

            if (JWT_UTIL.isTokenExpired(authCacheData.getAccessTokenClaimsSet().getExpirationTime()))
                throw new AuthException(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage(), 406);

            return AuthPayloadDto.of(userId);
        } catch (AuthException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthException(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage(), 406);
        }
    }

    private AuthResponse generateToken(String userId, int refreshTokenExpirationCount) {
        var accessTokenClaimsSet = TOKEN_MAPPER.buildAccessTokenClaimsSet(
                userId,
                JWT_UTIL.generateSessionExpirationTime(tokenExpirationProperties.getAccessToken())
        );

        var refreshTokenClaimsSet = TOKEN_MAPPER.buildRefreshTokenClaimsSet(
                userId,
                refreshTokenExpirationCount,
                JWT_UTIL.generateSessionExpirationTime(tokenExpirationProperties.getRefreshToken())
        );

        var keyPair = CERTIFICATE_KEY_UTIL.generateKeyPair();

        var authCacheData = AuthCacheData.of(
                accessTokenClaimsSet,
                encodeToString(keyPair.getPublic().getEncoded())
        );

        cacheProvider.updateToCache(authCacheData, userId, CACHE_EXPIRE_SECONDS);

        var privateKey = keyPair.getPrivate();
        var accessToken = JWT_UTIL.generateToken(accessTokenClaimsSet, privateKey);
        var refreshToken = JWT_UTIL.generateToken(refreshTokenClaimsSet, privateKey);

        return AuthResponse.of(accessToken, refreshToken);
    }

    private void verifyRefreshToken(RefreshTokenClaimsSet refreshTokenClaimsSet) {
        if (JWT_UTIL.isRefreshTokenTimeExpired(refreshTokenClaimsSet))
            throw new AuthException(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage(), 401);

        if (JWT_UTIL.isRefreshTokenCountExpired(refreshTokenClaimsSet))
            throw new AuthException(REFRESH_TOKEN_EXPIRED.getCode(), REFRESH_TOKEN_EXPIRED.getMessage(), 401);
    }

    private AuthCacheData fetchFromCache(String cacheKey) {
        return cacheProvider.getBucket(cacheKey);
    }


}
