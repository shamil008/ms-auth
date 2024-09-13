package org.example.msauth.client.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.example.msauth.exception.CustomFeignException;

import static org.example.msauth.client.decoder.JsonNodeFiledName.CODE;
import static org.example.msauth.client.decoder.JsonNodeFiledName.MESSAGE;
import static org.example.msauth.model.enums.ExceptionConstants.CLIENT_ERROR;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        var errorMessage = CLIENT_ERROR.getMessage();
        var errorCode = CLIENT_ERROR.getCode();
        JsonNode jsonNode;

        try (var body = response.body().asInputStream()){
            jsonNode = new ObjectMapper().readValue(body,JsonNode.class);
        }
        catch (Exception e){
            throw new CustomFeignException(errorMessage,errorCode,response.status());
        }
        if(jsonNode.has(MESSAGE.getValue())){
            errorMessage = jsonNode.get(MESSAGE.getValue()).asText();
        }
        if(jsonNode.has(CODE.getValue())){
            errorCode = jsonNode.get(CODE.getValue()).asText();
        }
        return new CustomFeignException(errorMessage,errorCode, response.status());
    }
}
