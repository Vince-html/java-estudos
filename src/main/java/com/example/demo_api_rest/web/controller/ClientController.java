package com.example.demo_api_rest.web.controller;


import com.example.demo_api_rest.entity.Client;
import com.example.demo_api_rest.jwt.JwtUserDetails;
import com.example.demo_api_rest.repository.projection.ClientProjection;
import com.example.demo_api_rest.service.ClientService;
import com.example.demo_api_rest.service.UsuarioService;
import com.example.demo_api_rest.web.dto.Cliente.ClienteCreateDto;
import com.example.demo_api_rest.web.dto.Cliente.ClienteResponseDto;
import com.example.demo_api_rest.web.dto.Cliente.PageableDto;
import com.example.demo_api_rest.web.dto.mapper.ClienteMapper;
import com.example.demo_api_rest.web.dto.mapper.PageableMapper;
import com.example.demo_api_rest.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@Tag(name="Clientes", description = "Contém o crud dos clientes")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clientes")
public class ClientController {

    private final ClientService clientService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente.",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode= "409",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(
                    responseCode= "422",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@Valid @RequestBody ClienteCreateDto createDto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClienteMapper.toCliente(createDto);
        client.setUsuario(usuarioService.findById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toClienteDto(client));
    };

    @Operation(summary = "Buscar clientes por ID.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode= "404",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
       Client client = clientService.findById(id);
        return ResponseEntity.ok(ClienteMapper.toClienteDto(client));
    };

    @Operation(summary = "Buscar clientes",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name= "page", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "valor inicial da pagina"),
                    @Parameter(in = ParameterIn.QUERY, name= "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "10")),
                            description = "valor inicial do size"),
                    @Parameter(in = ParameterIn.QUERY, name= "sort", hidden = true, content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Ordenação")
            },

            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        Page<ClientProjection> clientes = clientService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    };

    @Operation(summary = "Buscar clientes",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode= "403",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),

            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getUniqueClient(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Client cliente = clientService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toClienteDto(cliente));
    };
}
