package com.example.users.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.users.beans.timesheetbeans.DaysOff;
import com.example.users.beans.timesheetbeans.Timesheet;
import com.example.users.logic.repositories.DaysOffRepository;
import com.example.users.logic.repositories.TimesheetRepository;
import com.example.users.logic.services.TimesheetService;

@Controller
public class AdminTimesheetController {
	
	private TimesheetRepository timesheetRepository;
	private TimesheetService timesheetService;
	private DaysOffRepository daysOffRepository;
	
	public AdminTimesheetController(TimesheetRepository timesheetRepository, TimesheetService timesheetService, DaysOffRepository daysOffRepository) {
		super();
		this.timesheetRepository = timesheetRepository;
		this.timesheetService = timesheetService;
		this.daysOffRepository = daysOffRepository;
	}

	@GetMapping("/admin/timesheets")
	public String viewAllTimesheetsGroupedByTask(Model model) {
		List<Timesheet> allTimesheets = timesheetRepository.findAll();
	    Map<String, List<Timesheet>> timesheetsByTask = allTimesheets.stream()
	        .collect(Collectors.groupingBy(Timesheet::getTask));
	    model.addAttribute("timesheetsByTask", timesheetsByTask);
	    
	    Map<String, Set<String>> duplicates = timesheetService.detectDuplicatedTimesheetDatesAdmin();
	    model.addAttribute("duplicates", duplicates);
	    return "admin-timesheet-view";
	}
	
	@PostMapping("/approve-timesheet")
	public String approveTimesheet(@RequestParam("id") Long id) {
		Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(id);
		if(optionalTimesheet.isPresent()) {
			Timesheet timesheet = optionalTimesheet.get();
			timesheet.setApproved(true);
			timesheetRepository.save(timesheet);
		}
		return "redirect:/admin/timesheets";
	}
	
	@PostMapping("/redo-timesheet")
	public String UnApproveTimesheet(@RequestParam("id") Long id) {
		Optional<Timesheet> optionalTimesheet = timesheetRepository.findById(id);
		if(optionalTimesheet.isPresent()) {
			Timesheet timesheet = optionalTimesheet.get();
			timesheet.setApproved(false);
			timesheetRepository.save(timesheet);
		}
		return "redirect:/admin/timesheets";
	}
	
	@GetMapping("/admin/daysoff")
	public String viewAllDaysoffByTask(Model model) {
		List<DaysOff> allDaysOff = daysOffRepository.findAll();
	    model.addAttribute("daysoff", allDaysOff);		
		return "admin-daysoff";
	}
	
	@PostMapping("/approve-dayoff")
	public String approveDayoff(@RequestParam("id") Long id) {
		Optional<DaysOff> optionalDaysoff = daysOffRepository.findById(id);
		if(optionalDaysoff.isPresent()) {
			DaysOff dayoff = optionalDaysoff.get();
			dayoff.setApproved(true);
			daysOffRepository.save(dayoff);
		}
		return "redirect:/admin/daysoff";
	}
	
	@PostMapping("/unApprove-dayoff")
	public String UnApproveDayoff(@RequestParam("id") Long id) {
		Optional<DaysOff> optionalDaysoff = daysOffRepository.findById(id);
		if(optionalDaysoff.isPresent()) {
			DaysOff dayoff = optionalDaysoff.get();
			dayoff.setApproved(false);
			daysOffRepository.save(dayoff);
		}
		return "redirect:/admin/daysoff";
	}
	
	@PostMapping("/setConsume-dayoff")
	public String setConsumeDayoff(@RequestParam("id") Long id) {
		Optional<DaysOff> optionalDaysoff = daysOffRepository.findById(id);
		if(optionalDaysoff.isPresent()) {
			DaysOff dayoff = optionalDaysoff.get();
			dayoff.setConsumed(true);
			daysOffRepository.save(dayoff);
		}
		return "redirect:/admin/daysoff";
	}
	
	@PostMapping("/setUnConsume-dayoff")
	public String setUnConsumeDayoff(@RequestParam("id") Long id) {
		Optional<DaysOff> optionalDaysoff = daysOffRepository.findById(id);
		if(optionalDaysoff.isPresent()) {
			DaysOff dayoff = optionalDaysoff.get();
			dayoff.setConsumed(false);
			daysOffRepository.save(dayoff);
		}
		return "redirect:/admin/daysoff";
	}
	
	@GetMapping("/manage")
	public String manage() {
		return "admin-dashboard";
	}
}
