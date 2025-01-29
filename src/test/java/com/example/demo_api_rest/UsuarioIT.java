package com.example.demo_api_rest;


import com.example.demo_api_rest.web.dto.Usuarios.UsuarioCreateDto;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioResponseDto;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioSenhaDto;
import com.example.demo_api_rest.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class UsuarioIT {
    @Autowired
    WebTestClient testClient;

    @Value("${api.url}")
    private String apiUrl;

    @Test
    public void createUser_userNameEmailPasswordValid_ReturnUserStatus201() {
        UsuarioResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vicente@email.com", "12345678", "vicente"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("vicente");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void createUser_EmailPassWordUserNameInvalid_ReturnUserStatus402() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "12345678", "vicente"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vicente@email.com", "", "vicente"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vicente@email.com", "12345678", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }

    @Test
    public void createUser_userNameDuplicate_ReturnUserStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vicente.maga@live.com", "12345678", "vicente"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);


    }


    @Test
    public void getUser_userNameEmailPasswordValid_ReturnUserStatus200() {
        UsuarioResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/1000")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("Vicente");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/1001")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("Bia");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/1002")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("Bob");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }



    @Test
    public void getUser_NotFound_ReturnUserStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/2000")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);


    }

    @Test
    public void getUser_Forbi_ReturnUserStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/1001")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "bob.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }

    @Test
    public void EditPassword_ValidData_ReturnStatus200() {
       testClient
                .patch()
                .uri("/api/v1/usuarios/1000")
               .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "1234567", "1234567"))
               .exchange()
               .expectStatus().isOk();




    }

    @Test
    public void changePass_isForbidden_ReturnUserStatus403() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/10011")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "1234567", "1234567"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }
    @Test
    public void changePass_processError_ReturnUserStatus400() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/1000")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345611", "1234567", "1234567"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        testClient
                .patch()
                .uri("/api/v1/usuarios/1000")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345611", "123057", "1234567"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }
    @Test
    public void changePass_processError_ReturnUserStatus422() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/usuarios/1000")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("1234", "1234567", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void getUserController_userNameEmailPasswordValid_ReturnUserStatus200() {
        List<UsuarioResponseDto> expectedUsers = List.of(
                new UsuarioResponseDto(1000L, "Vicente", "vicente.maga@live.com", "ADMIN")
        );
        List<UsuarioResponseDto> responseBody = testClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios")

                        .queryParam("username", "Vicente")
                        .build())
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody)
                .isNotNull()
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedUsers);


        testClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios")
                        .queryParam("email", "vicente.maga@live.com")
                        .build())
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody)
                .isNotNull()
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(expectedUsers);

    }

    @Test
    public void getAllUserController_userNameEmailPasswordValid_ReturnUserStatus200() {
        List<UsuarioResponseDto> responseBody  = testClient
                .get()
                .uri("/api/v1/usuarios")
                .headers(JwtAuthClass.getHeaderAuthorization(testClient, "vicente.maga@live.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody)
                .isNotNull()
                .hasSize(3);

    }
}
