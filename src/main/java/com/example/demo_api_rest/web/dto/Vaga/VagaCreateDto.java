package com.example.demo_api_rest.web.dto.Vaga;


import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VagaCreateDto {

    @NotBlank
    @Size(min=4, max=4)
    private String codigo;

    @NotBlank
    @Pattern(regexp = "LIVRE|OCUPADA")
    private String status;
}
