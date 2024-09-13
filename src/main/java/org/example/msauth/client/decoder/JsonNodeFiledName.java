package org.example.msauth.client.decoder;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JsonNodeFiledName {
    MESSAGE("message"),
    CODE("code");
    private final String value;
}