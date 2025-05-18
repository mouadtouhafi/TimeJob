package com.example.Jwt.example.jwtsecurity;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@GetMapping("/login")
	public String loginPage(Principal principal) {
		if (principal != null) {
	        return "redirect:/home"; 
	    }
	    return "login";
	}

	@PostMapping("/authenticate")
	public String createAuthenticationToken(@ModelAttribute AuthenticationRequest authenticationRequest,
			HttpServletResponse response) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), 
					authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			return "redirect:login";
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		Map<String, Object> claims = new HashMap<>();
		claims.put(
				"roles", 
				userDetails.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList())
		);
		
		/*
		 * 
		 * To persist the JWT on the client, the code creates a new Cookie named
		 * "JWT_TOKEN" with the JWT string as its value. It sets
		 * cookie.setHttpOnly(true) to prevent access via client-side scripts,
		 * mitigating XSS risks. By calling cookie.setSecure(true), the cookie is
		 * constrained to HTTPS transmissions, protecting it from interception over
		 * insecure channels. The path is set to "/" so the cookie is included in
		 * requests to the entire application context, and cookie.setMaxAge(3600)
		 * configures the browser to discard the cookie after one hour.
		 * 
		 */
		final String jwt = jwtService.generateToken(userDetails);
		Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setSecure(false);
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(3600);

		response.addCookie(jwtCookie);

		return "home";
	}
}
