package com.example.Jwt.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.Jwt.example.jwtsecurity.JwtAuthenticationEntryPoint;
import com.example.Jwt.example.jwtsecurity.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecureWeb {

	private JwtRequestFilter jwtRequestFilter;
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	public SecureWeb(GetUserDetails getUserDetails, JwtRequestFilter jwtRequestFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		super();
		this.jwtRequestFilter = jwtRequestFilter;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers("/authenticate", "/login", "/register", "/about", "/webjars/**", "/error/**", "/images/**","/js/**","/stylesheets/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated())
				.exceptionHandling(exc -> exc
						.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.csrf(csrf -> csrf
						.ignoringRequestMatchers("/h2-console/**")
						.disable())
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
			    .logout(logout -> logout
			        .logoutUrl("/logout")
			        .deleteCookies("JWT_TOKEN")
			        .logoutSuccessUrl("/login")
			        .permitAll()
			    );


		return http.build();
	}

//	@Bean
//	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//		authenticationManagerBuilder.userDetailsService(getUserDetails).passwordEncoder(passwordEncoder());
//		return authenticationManagerBuilder.build();
//	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
