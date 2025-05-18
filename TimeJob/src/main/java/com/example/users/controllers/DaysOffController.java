package com.example.users.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.users.beans.timesheetbeans.DaysOff;
import com.example.users.beans.userbeans.WebUser;
import com.example.users.logic.repositories.DaysOffRepository;
import com.example.users.logic.repositories.UserRepository;
import com.example.users.logic.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DaysOffController {
	
	private UserRepository userRepository;
	private DaysOffRepository daysOffRepository;
	private UserService userService;
	
	public DaysOffController(UserRepository userRepository, DaysOffRepository daysOffRepository, UserService userService) {
		super();
		this.userRepository = userRepository;
		this.daysOffRepository = daysOffRepository;
		this.userService = userService;
	}

	@GetMapping("/days-off")
	public String days_off() {
		return "days-off";
	}
	
	@PostMapping("/submit-days-off")
	public String submitDaysOff(@RequestParam("selectedDates") String selectedDatesJson, Principal principal) throws JsonProcessingException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<String> selectedDates = objectMapper.readValue(selectedDatesJson, new TypeReference<List<String>>() {});
	    
	    Optional<WebUser> optionalUser = userRepository.findByUsername(principal.getName());
	    if(optionalUser.isPresent()) {
	        WebUser user = optionalUser.get();
	        for (String dateStr : selectedDates) {
	            boolean alreadyExists = daysOffRepository.findByDateAndUserId(dateStr, user.getId()).isPresent();
	            if (!alreadyExists) {
	                DaysOff daysOff = new DaysOff();
	                daysOff.setDate(dateStr);
	                daysOff.setUser(user);
	                daysOff.setApproved(false);
	                daysOff.setConsumed(false);
	                daysOffRepository.save(daysOff);
	            }
	        }
	    }
	    return "redirect:/days-off";
	}
	
	@GetMapping("/daysoff-list")
	public String listSubmittedDaysoff(Model daysOffModel, Principal principal) {
		String username = userService.getCurrentUsername(principal);
		Long currentUserId = userService.getCurrentUserId(username);
		List<DaysOff> daysoff = daysOffRepository.findByUserId(currentUserId);
		daysOffModel.addAttribute("daysoff", daysoff);
		return "daysoff-submitted";
	}
	
	@PostMapping("/delete-dayoff")
	public String deleteDayOff(@RequestParam Long id, Principal principal) {
	    daysOffRepository.deleteById(id);
		return "redirect:/daysoff-list";
	}
}
