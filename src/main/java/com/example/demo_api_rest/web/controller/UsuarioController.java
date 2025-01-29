package com.example.demo_api_rest.web.controller;

import com.example.demo_api_rest.entity.Usuario;
import com.example.demo_api_rest.exception.EntityNotFoundException;
import com.example.demo_api_rest.exception.PasswordInvalidException;
import com.example.demo_api_rest.service.UsuarioService;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioCreateDto;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioResponseDto;
import com.example.demo_api_rest.web.dto.Usuarios.UsuarioSenhaDto;
import com.example.demo_api_rest.web.dto.mapper.UsuarioMapper;
import com.example.demo_api_rest.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name="Usuários", description = "Contém o crud dos usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo usuário.", responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode= "409",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(
                    responseCode= "422",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
         Usuario user = usuarioService.save(UsuarioMapper.toUsuario(createDto));
         return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    };


    @Operation(summary = "Buscar usuário por ID.",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode= "404",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.findById(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    };

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND (#id == authentication.principal.id)")
    @Operation(summary = "Atualizar senha",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode= "404",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityNotFoundException.class))),
            @ApiResponse(responseCode= "400",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PasswordInvalidException.class))),
            @ApiResponse(
                    responseCode= "422",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode= "403",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),

    })
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable Long id,@Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.changePass(id, dto.getPassword(), dto.getNew_password(), dto.getNew_password_confirm());
        Map<String, String> response = Map.of("message", "Senha alterada com sucesso!");
        return ResponseEntity.ok(response);
    };

    @Operation(summary = "Buscar usuários",
            security = @SecurityRequirement(name = "security"),

            responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode= "404",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> getAll(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email
    ) {
        List<Usuario> users;

        if (username != null && !username.isBlank()) {
            users = usuarioService.findByName(username);
        } else if (email != null && !email.isBlank()) {
            users = usuarioService.findByEmail(email);
        } else {
            users = usuarioService.findAll();
        }
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    };
}
