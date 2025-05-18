package com.example.Jwt.example.jwtsecurity;

/*
 * 
 * The AuthenticationResponse class is a simple immutable Data Transfer Object (DTO) that holds the 
 * JWT string generated upon successful authentication, allowing Spring Boot to serialize and return 
 * a concise JSON object to clients without exposing domain logic.
 * ​
*/
public class AuthenticationResponse {
	private final String jwt;
	
	public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
    
    public String getJwt() {
        return jwt;
    }
}
