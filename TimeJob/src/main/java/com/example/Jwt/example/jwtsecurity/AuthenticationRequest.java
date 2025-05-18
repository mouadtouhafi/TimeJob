package com.example.Jwt.example.jwtsecurity;

/*
 * 
 * This is a simple Data Transfer Object that encapsulates the username and password fields submitted by a client, 
 * allowing Spring MVC to bind incoming form parameters (via @ModelAttribute) or JSON payloads directly into its properties without any additional business logic. 
 * Acting purely as a container with standard getters and setters, 
 * it cleanly separates credential transport from authentication processing, 
 * improving maintainability and clarity in the controller layer
 * 
 * */
public class AuthenticationRequest {
	private String username;
	private String password;

	public AuthenticationRequest() {

	}

	public AuthenticationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
