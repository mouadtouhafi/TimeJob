package com.example.Jwt.example.jwtsecurity;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


	/*
	 * 
	 * This class implements Spring Security’s AuthenticationEntryPoint interface, 
	 * intercepting unauthorized access attempts and immediately sending an HTTP 401 Unauthorized response to the client
	 * 
	 * */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
