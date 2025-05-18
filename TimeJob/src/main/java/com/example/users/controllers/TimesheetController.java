package com.example.users.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.users.beans.timesheetbeans.Timesheet;
import com.example.users.beans.userbeans.WebUser;
import com.example.users.logic.repositories.TimesheetRepository;
import com.example.users.logic.services.TimesheetService;
import com.example.users.logic.services.UserService;

import jakarta.validation.Valid;

@Controller
public class TimesheetController {
	
	private TimesheetRepository timesheetRepository;
	private UserService userService;
	private TimesheetService timesheetService;
	
	public TimesheetController(TimesheetRepository timesheetRepository, UserService userService, TimesheetService timesheetService) {
		this.timesheetRepository = timesheetRepository;
		this.userService = userService;
		this.timesheetService = timesheetService;
	}
	
	public String sanitizeHour(String hour) {
	    return (hour != null && !hour.equals("-")) ? hour : "-";
	}
	
	@GetMapping("/timesheet")
	public String getTimesheet(Model model) {
		Timesheet timesheet = new Timesheet();
		model.addAttribute("timesheet", timesheet);
		List<String> tasks = Arrays.asList("Software development", "Test / Validation", "Training", "Bench");
		model.addAttribute("tasks", tasks);
		return "timesheet-entry-page";
	}
	
	@PostMapping("/submit-timesheet")
	public String submitTimesheet(@Valid Timesheet timesheet, BindingResult result, Principal principal, Model model, RedirectAttributes ra) {
		List<String> fieldsOrder = Arrays.asList("startDate", "endDate", "task", "totalHours", "notes");
		List<String> tasks = Arrays.asList("Software development", "Test / Validation", "Training", "Bench");
		if(result.hasErrors()) {
			String firstFieldError = fieldsOrder.stream()
												.filter(field -> result.hasFieldErrors(field))
												.findFirst()
												.orElse(null);
			if(firstFieldError != null) {
				String errorMessage = result.getFieldError(firstFieldError).getDefaultMessage();
				model.addAttribute("errorMessage", errorMessage);
			}
			model.addAttribute("tasks", tasks);
			return "timesheet-entry-page";
		}
		String username = userService.getCurrentUsername(principal);
		model.addAttribute("username", username);
		
		WebUser currentUser = userService.findWebUser(username);
		timesheet.setUser(currentUser);
		timesheet.setApproved(false);
		
		if(timesheet.getMondayHours() == null) timesheet.setMondayHours("-");
		if(timesheet.getTuesdayHours() == null) timesheet.setTuesdayHours("-");
		if(timesheet.getWednesdayHours() == null) timesheet.setWednesdayHours("-");
		if(timesheet.getThursdayHours() == null) timesheet.setThursdayHours("-");
		if(timesheet.getFridayHours() == null) timesheet.setFridayHours("-");
		if(timesheet.getSaturdayHours() == null) timesheet.setSaturdayHours("-");
		if(timesheet.getSundayHours() == null) timesheet.setSundayHours("-");
		
		timesheetRepository.save(timesheet);
		
		model.addAttribute("successMessage", "Timesheet saved successfully!");
	    model.addAttribute("tasks", tasks);
	    model.addAttribute("timesheet", new Timesheet());
	    ra.addFlashAttribute("successMessage", "Timesheet saved successfully!");
		return "redirect:/timesheet";
	}
	
	@GetMapping("/timesheet-list")
	public String listSubmittedTimesheets(Model timesheetModel, Principal principal) {
		String username = userService.getCurrentUsername(principal);
		Long currentUserId = userService.getCurrentUserId(username);
		List<Timesheet> timesheet = timesheetRepository.findByUserId(currentUserId);
		timesheetModel.addAttribute("timesheets", timesheet);
		timesheetModel.addAttribute("duplicatedDates", timesheetService.detectTimesheetDuplicatedDates(principal));
		return "timesheet-submitted";
	}
	
	@GetMapping("/timesheet/update/{id}")
	public String showUpdateTimesheet(@PathVariable Long id, Model model) {
		List<String> tasks = Arrays.asList("Software development", "Test / Validation", "Training", "Bench");
		model.addAttribute("tasks", tasks);
		Timesheet timesheet = timesheetRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
		timesheet.setTotalHours("0");
		model.addAttribute("timesheet", timesheet);	
		return "timesheet-update";
	}
	
	@PostMapping("/update-timesheet")
	public String submitUpdatedTimesheet(@Valid Timesheet timesheet, BindingResult result, Principal principal, Model model, RedirectAttributes ra) {
		Optional<Timesheet> timesheetToUpdate = timesheetRepository.findById(timesheet.getId());
		if(timesheetToUpdate.isPresent()) {
			Timesheet ts = timesheetToUpdate.get();
			ts.setTask(timesheet.getTask());
			ts.setNotes(timesheet.getNotes());
			ts.setTotalHours(timesheet.getTotalHours());
			
			ts.setMondayHours(sanitizeHour(timesheet.getMondayHours()));
			ts.setTuesdayHours(sanitizeHour(timesheet.getTuesdayHours()));
			ts.setWednesdayHours(sanitizeHour(timesheet.getWednesdayHours()));
			ts.setThursdayHours(sanitizeHour(timesheet.getThursdayHours()));
			ts.setFridayHours(sanitizeHour(timesheet.getFridayHours()));
			ts.setSaturdayHours(sanitizeHour(timesheet.getSaturdayHours()));
			ts.setSundayHours(sanitizeHour(timesheet.getSundayHours()));
			timesheetRepository.save(ts);
			
			return "redirect:/timesheet-list";
		}
		return "redirect:/timesheet";
	}
	
	@PostMapping("/delete-timesheet")
	public String deleteTimesheet(@RequestParam Long id, Principal principal) {
	    timesheetRepository.deleteById(id);
		return "redirect:timesheet-list";
	}
	
	@GetMapping("/submissions")
	public String submissions() {
		return "submissions";
	}
}
