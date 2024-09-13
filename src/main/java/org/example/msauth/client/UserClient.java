package org.example.msauth.client;

import org.example.msauth.client.decoder.CustomErrorDecoder;
import org.example.msauth.model.client.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ms-user",
        path = "/internal",
        url = "${client.urls.ms-user}",
        configuration = CustomErrorDecoder.class
)
public interface UserClient {

    @GetMapping("/v1/users")
    UserResponseDto getUserDetails(@RequestParam String username);
}