package com.example.demo_api_rest;


import com.example.demo_api_rest.jwt.JwtToken;
import com.example.demo_api_rest.web.dto.Usuarios.UserLoginDTO;

import com.example.demo_api_rest.web.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;




@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthIT {


        @Autowired
        WebTestClient testClient;

        @Value("${api.url}")
        private String apiUrl;

        @Test
        public void loginSuccess_returnStatus200() {
            JwtToken responseBody  = testClient
                    .post()
                    .uri("/api/v1/auth")
                    .bodyValue(new UserLoginDTO("vicente.maga@live.com", "12345678"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(JwtToken.class)
                    .returnResult().getResponseBody();


            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getToken()).isNotNull();


        }

    @Test
    public void loginSuccess_returnStatus400() {
        ErrorMessage responseBody  = testClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UserLoginDTO("Vicente", "123452678"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    }

}
