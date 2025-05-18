package com.example.users.controllers;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.users.beans.userbeans.RegistrationForm;
import com.example.users.beans.userbeans.Roles;
import com.example.users.beans.userbeans.WebUser;
import com.example.users.logic.repositories.RoleRepository;
import com.example.users.logic.repositories.UserRepository;
import com.example.users.logic.services.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	
	
	public UserController(UserRepository userRepository, RoleRepository roleRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String showSignInPage(ModelMap model) {		
		model.addAttribute("form", new RegistrationForm());
	    return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String signInPage(ModelMap model, @Valid @ModelAttribute("form") RegistrationForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "register";
		}
		if (userRepository.existsByUsername(form.getUsername())) {
			result.rejectValue("username", "error.user", "Username already exists.");
			return "register";
		}
		WebUser user = new WebUser();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(UserService.encryptPassword(form.getPassword()));
		
		user.setPassword(UserService.encryptPassword(form.getPassword()));
		Roles userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new IllegalStateException("Default role ROLE_USER not found"));
		user.setRoles(Set.of(userRole));
		userRepository.save(user);
		return "redirect:login";
	}

	
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
}
