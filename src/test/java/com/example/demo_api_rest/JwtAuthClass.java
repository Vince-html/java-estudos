package com.example.demo_api_rest;

import com.example.demo_api_rest.jwt.JwtToken;
import com.example.demo_api_rest.web.dto.Usuarios.UserLoginDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthClass {

    public static Consumer<HttpHeaders> getHeaderAuthorization (WebTestClient client,String email, String password) {
        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UserLoginDTO(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
