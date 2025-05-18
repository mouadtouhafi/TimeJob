package com.example.Jwt.example;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.users.beans.userbeans.Roles;
import com.example.users.beans.userbeans.WebUser;
import com.example.users.logic.repositories.UserRepository;

@Service
public class GetUserDetails implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public GetUserDetails(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<WebUser> optionalUser = userRepository.findByUsername(username);
		WebUser webUser = optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found : " +username));
		
		List<SimpleGrantedAuthority> authorities = webUser.getRoles()
				.stream()
				.map(Roles::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
				
		
		return User.builder()
					.username(webUser.getUsername())
					.password(webUser.getPassword())
					.authorities(authorities)
				.build();
	}

}
