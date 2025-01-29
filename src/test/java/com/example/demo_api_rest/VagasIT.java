package com.example.demo_api_rest;

import com.example.demo_api_rest.web.dto.Vaga.VagaCreateDto;
import com.example.demo_api_rest.web.dto.Vaga.VagaResponseDto;
import com.example.demo_api_rest.web.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagasIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void getVaga_dataValid_ReturnUserStatus201() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(new VagaCreateDto("aoqa", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void getVaga_CONFLICT_ReturnUserStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(new VagaCreateDto("abce", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void getVaga_dataValid_ReturnUserStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(new VagaCreateDto("", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void getVagaByCodigo_dataValid_ReturnUserStatus200() {
        VagaResponseDto responseDto = testClient
                .get()
                .uri("/api/v1/vagas/abcd")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(VagaResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo("LIVRE");
        org.assertj.core.api.Assertions.assertThat(responseDto.getCodigo()).isEqualTo("abcd");
    }

    @Test
    public void getVagaByCodigo_NOTFOUND_ReturnUserStatus404() {
        ErrorMessage responseDto = testClient
                .get()
                .uri("/api/v1/vagas/zzzz")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(404);

    }
    @Test
    public void getVagaByCodigo_isForbidden_ReturnUserStatus403() {
        ErrorMessage responseDto = testClient
                .get()
                .uri("/api/v1/vagas/abcd")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getStatus()).isEqualTo(403);
    }

}

