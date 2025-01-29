package com.example.demo_api_rest.web.dto.Usuarios;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDto {
    @NotBlank
    @Size(min=6, max=10)
    private String password;
    @NotBlank
    @Size(min=6, max=10)
    private String new_password;
    @NotBlank
    @Size(min=6, max=10)
    private String new_password_confirm;
}
