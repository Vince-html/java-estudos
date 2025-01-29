package com.example.demo_api_rest.web.dto.Usuarios;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDTO {
    private String email;
    private String password;
}
