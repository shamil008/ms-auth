package org.example.msauth.model.jwt;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenClaimsSet {
    private String userId;
    private Date exp;
    private Integer count;
    private String iss;
}
