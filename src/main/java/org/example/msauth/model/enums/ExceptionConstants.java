package org.example.msauth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionConstants {
    UNEXPECTED_EXCEPTION("UNEXPECTED_EXCEPTION","Unexpected exception occurred"),
    USER_UNAUTHORIZED("USER UNAUTHORIZED","User unauthorized"),
    CLIENT_ERROR("CLIENT_EXCEPTION","Exception from client"),
    TOKEN_EXPIRED("TOKEN_EXPIRED","Token expired"),
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED","Token expired");
    private String code;
    private String message;
}
