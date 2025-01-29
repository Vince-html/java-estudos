package com.example.demo_api_rest;

import com.example.demo_api_rest.web.dto.Cliente.ClienteCreateDto;
import com.example.demo_api_rest.web.dto.Cliente.ClienteResponseDto;
import com.example.demo_api_rest.web.dto.Cliente.PageableDto;
import com.example.demo_api_rest.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createClient_dataValid_ReturnUserStatus201() {
        ClienteResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .bodyValue(new ClienteCreateDto("John De volta", "04500739041"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("John De volta");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("04500739041");

    }

    @Test
    public void createClient_CONFLITO_ReturnUserStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .bodyValue(new ClienteCreateDto("John De volta", "56637623024"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    static Stream<ClienteCreateDto> invalidClientsProvider() {
        return Stream.of(
                new ClienteCreateDto("John De volta", ""),         // CPF vazio
                new ClienteCreateDto("John", "04500739041"),       // Nome curto
                new ClienteCreateDto("John de volta", "12345678901") // CPF inválido
        );
    }

    @ParameterizedTest
    @MethodSource("invalidClientsProvider")
    public void createClient_ISInvalid_ReturnUserStatus422(ClienteCreateDto clienteDto) {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .bodyValue(clienteDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void adminCreateClient_NoAdmin_ReturnUserStatus403() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .bodyValue(new ClienteCreateDto("John De volta", "56637623024"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }

    @Test
    public void getClienteByUser_returnValid_ReturnUserStatus200() {
        ClienteResponseDto responseBody  = testClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Bob Bertioga");


    }

    @Test
    public void getClienteByUser_returnNotFound_ReturnUserStatus404() {
        ErrorMessage responseBody  = testClient
                .get()
                .uri("/api/v1/clientes/111")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);


    }

    @Test
    public void getClienteByUser_returnForbi_ReturnUserStatus403() {
        ErrorMessage responseBody  = testClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }

    @Test
    public void getClienteByUser_returnClientes_ReturnUserStatus200() {
        PageableDto responseBody  = testClient
                .get()
                .uri("/api/v1/clientes?size=1&page=1")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);


    }

    @Test
    public void getAllClient_returnForbi_ReturnUserStatus403() {
        ErrorMessage responseBody  = testClient
                .get()
                .uri("/api/v1/clientes?size=1&page=1")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "john_devolta@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }

    @Test
    public void getCliente_returnCliente_ReturnUserStatus200() {
        ClienteResponseDto responseBody  = testClient
                .get()
                .uri("/api/v1/clientes/details")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Bob Bertioga");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("56637623024");
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
    }

    @Test
    public void getCliente_returnCliente_ReturnUserStatus403() {
        ErrorMessage responseBody  = testClient
                .get()
                .uri("/api/v1/clientes/details")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();

        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


}
