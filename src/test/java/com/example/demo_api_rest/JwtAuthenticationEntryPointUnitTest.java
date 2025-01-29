package com.example.demo_api_rest;


import com.example.demo_api_rest.jwt.JwtAuthenticationEntryPoint;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class JwtAuthenticationEntryPointUnitTest {

    @Test
    void testCommence() throws IOException, ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        when(authException.getMessage()).thenReturn("Usuário não autenticado");

        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

        entryPoint.commence(request, response, authException);

        verify(response).setHeader("www-authenticate", "Bearer realm='/api/vi/auth'");

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
