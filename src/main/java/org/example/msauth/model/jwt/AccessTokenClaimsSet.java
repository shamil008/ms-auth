package org.example.msauth.model.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenClaimsSet implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;

    private String iss;

    @JsonProperty("exp")
    private Date expirationTime;

    @JsonProperty("iat")
    private Date createdTime;

}
