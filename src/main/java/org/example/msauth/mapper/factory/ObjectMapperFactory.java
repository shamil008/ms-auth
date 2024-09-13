package org.example.msauth.mapper.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public enum ObjectMapperFactory {
    OBJECT_MAPPER_FACTORY;

    public ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}