package org.example.msauth.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.msauth.model.jwt.AccessTokenClaimsSet;
import org.example.msauth.model.jwt.RefreshTokenClaimsSet;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;
import static org.example.msauth.model.constants.AuthConstants.ISSUER;
@NoArgsConstructor(access = PRIVATE)
public enum TokenMapper {
    TOKEN_MAPPER;

    public AccessTokenClaimsSet buildAccessTokenClaimsSet(String userId, Date expirationTime) {
        return AccessTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(userId)
                .createdTime(new Date())
                .expirationTime(expirationTime)
                .build();
    }

    public RefreshTokenClaimsSet buildRefreshTokenClaimsSet(String userId, int refreshTokenExpirationCount, Date expirationTime) {
        return RefreshTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(userId)
                .count(refreshTokenExpirationCount)
                .exp(expirationTime)
                .build();
    }
}
