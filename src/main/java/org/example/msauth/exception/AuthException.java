package org.example.msauth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private String code;
    private final int httpStatusCode;

    public AuthException(String message, String code,int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.code = code;
    }
}
