package com.example.Jwt.example;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 * 
 * This class runs after a username/password form login succeeds.
 * 
 * */

public class AuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws ServletException, IOException {
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
		if(isAdmin){
			setDefaultTargetUrl("/home_admin");
		}else {
			setDefaultTargetUrl("/home_user");
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
