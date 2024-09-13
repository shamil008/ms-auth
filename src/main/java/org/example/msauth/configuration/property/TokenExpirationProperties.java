package org.example.msauth.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("token.expiration")
public class TokenExpirationProperties {
    private Integer accessToken;
    private Integer refreshToken;
}
