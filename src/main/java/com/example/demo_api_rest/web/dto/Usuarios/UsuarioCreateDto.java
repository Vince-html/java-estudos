package com.example.demo_api_rest.web.dto.Usuarios;


import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateDto {

    @NotBlank
    @Email(message = "Email invalido", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
    @NotBlank
    @Size(min=6, max=10)
    private String password;
    @NotBlank
    private String username;


}
