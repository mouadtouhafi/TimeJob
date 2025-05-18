package com.example.users.logic.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.users.beans.userbeans.Roles;
import com.example.users.beans.userbeans.WebUser;
import com.example.users.logic.repositories.UserRepository;

import io.jsonwebtoken.lang.Collections;


@Service
public class UserService{
	
	@Autowired
    private UserRepository userRepository;
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public static String encryptPassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
	
	

	public Optional<WebUser> getUserById(Long id) {
	    return userRepository.findById(id);
	}
	
	public String getCurrentUsername(Principal principal) {
		String username = principal.getName();
		return username;
	}
	
	public Long getCurrentUserId(String username) {
		WebUser webUser = findWebUser(username);
		Long currentUserId = webUser.getId();
		return currentUserId;
	}
    
    public WebUser findWebUser(String username) throws UsernameNotFoundException{
    	return userRepository.findByUsername(username)
    			.orElseThrow(() -> new UsernameNotFoundException("User not found : "+username));
    }
    
    public Set<Roles> getCurrentUserRoles(Principal principal){
    	String username = getCurrentUsername(principal);
    	if(username !=null) {
    		Optional<WebUser> user = userRepository.findByUsername(username);
    		if(user.isPresent()) {
    			return user.get().getRoles();
    		}else {
    			return Collections.emptySet();
    		}
    	}else {
    		throw new IllegalArgumentException("User not authenticated");
    	}
    }
    
    public List<String> rolesToStringConverter(Set<Roles> roles){
    	List<String> rolesConverted = new ArrayList<String>();
    	for(Roles role : roles) {
    		rolesConverted.add(role.getName());
    	}
    	return rolesConverted;
    }
}
