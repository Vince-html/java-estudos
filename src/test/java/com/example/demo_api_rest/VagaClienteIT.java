package com.example.demo_api_rest;


import com.example.demo_api_rest.web.dto.Cliente.PageableDto;
import com.example.demo_api_rest.web.dto.Estacionamento.EstacionamentoCreateDto;

import com.example.demo_api_rest.web.dto.Estacionamento.EstacionamentoResponseDto;
import com.example.demo_api_rest.web.exception.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamento-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamento-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaClienteIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void getVaga_dataValid_ReturnUserStatus201() {
        EstacionamentoCreateDto estacionamentoCreateDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
                .cor("AZUL").clienteCpf("56637623024")
                .build();
        testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(estacionamentoCreateDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("WER-1111")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO 1.0")
                .jsonPath("cor").isEqualTo("AZUL")
                .jsonPath("clienteCpf").isEqualTo("56637623024")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").exists()
                .jsonPath("vagaCodigo").exists();

    }

    static Stream<EstacionamentoCreateDto> invalidEstacionamentoProvider() {
        return Stream.of(
                new EstacionamentoCreateDto("ABC-1234", "", "COR", "LASLD", "56637623024"),
                new EstacionamentoCreateDto("234", "ka", "COR", "LASLD", "56637623024"),
                new EstacionamentoCreateDto("ABC-1234", "FORD", "", "LASLD", "56637623024"),
                new EstacionamentoCreateDto("ABC-1234", "FORD", "COR", "", "56637623024"),
                new EstacionamentoCreateDto("ABC-1234", "FORD", "COR", "LASLD", "")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidEstacionamentoProvider")
    public void checkin_isInvalid_ReturnUserStatus422(EstacionamentoCreateDto estacionamentoCreateDto) {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(estacionamentoCreateDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void getVaga_isForbidden_ReturnUserStatus403() {
        EstacionamentoCreateDto estacionamentoCreateDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
                .cor("AZUL").clienteCpf("56637623024")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .bodyValue(estacionamentoCreateDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public void getVaga_notFound_ReturnUserStatus404() {
        EstacionamentoCreateDto estacionamentoCreateDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
                .cor("AZUL").clienteCpf("09191773016")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(estacionamentoCreateDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Sql(scripts = "/sql/estacionamentos/estacionamento-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamento-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void getVaga_NOTFOUND_ReturnUserStatus404() {
        EstacionamentoCreateDto estacionamentoCreateDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
                .cor("AZUL").clienteCpf("98401203015")
                .build();
        testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(estacionamentoCreateDto)
                .exchange()
                .expectStatus().isNotFound();


    }

    @Test
    public void getReciboAdmin_EXISTS_ReturnUserStatus200() {

        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/20230313-101300")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2025-01-25T10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");


    }

    @Test
    public void getReciboClient_EXISTS_ReturnUserStatus200() {

        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/20230313-101300")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2025-01-25T10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");


    }

    @Test
    public void getRecibo_NotFound_ReturnUserStatus404() {

        ErrorMessage response = testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/20230313-101asa300")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class).returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(404);

    }


    @Test
    public void checkout_ComRecibo_ReturnUserStatus200() {

        testClient.put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2025-01-25T10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");


    }

    @Test
    public void checkout_ComReciboInvalido_ReturnUserStatus404() {

        ErrorMessage responseBody = testClient.put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-1013002")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);


    }

    @Test
    public void checkout_NotAuthorization_ReturnUserStatus403() {

        ErrorMessage responseBody = testClient.put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-1013002")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }

    @Test
    public void buscarEstacionamentos_PorClienteCpf_RetornarSucesso() {

        PageableDto responseBody = testClient.get()
                .uri("/api/v1/estacionamentos/{cpf}?size=1&page=0", "98401203015")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = testClient.get()
                .uri("/api/v1/estacionamentos/{cpf}?size=1&page=1", "98401203015")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void buscarEstacionamentos_PorClienteCpf_Retornar403() {

        ErrorMessage responseBody = testClient.get()
                .uri("/api/v1/estacionamentos/{cpf}?size=1&page=0", "98401203015")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


    @Test
    public void buscarEstacionamentos_DoClienteLogado_RetornarSucesso() {

        PageableDto responseBody = testClient.get()
                .uri("/api/v1/estacionamentos?size=1&page=0")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = testClient.get()
                .uri("/api/v1/estacionamentos?size=1&page=1")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void buscarEstacionamentos_DoClienteLogadoPerfilAdmin_RetornarErrorStatus403() {

        testClient.get()
                .uri("/api/v1/estacionamentos")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos")
                .jsonPath("method").isEqualTo("GET");
    }
}
