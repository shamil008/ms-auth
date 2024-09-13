package org.example.msauth.util;

import java.io.IOException;
import java.io.InputStream;

import static org.example.msauth.mapper.factory.ObjectMapperFactory.OBJECT_MAPPER_FACTORY;

public enum MapperUtil {
    MAPPER_UTIL;

    public <T> T map(InputStream body, Class<T> clazz) {
        try {
            return OBJECT_MAPPER_FACTORY.createObjectMapper().readValue(body, clazz);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    public <T> T map(String body, Class<T> clazz) {
        try {
            return OBJECT_MAPPER_FACTORY.createObjectMapper().readValue(body, clazz);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    public <T> String map(T body) {
        try {
            return OBJECT_MAPPER_FACTORY.createObjectMapper().writeValueAsString(body);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
