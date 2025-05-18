package com.example.users.logic.services;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.users.beans.timesheetbeans.Timesheet;
import com.example.users.logic.repositories.TimesheetRepository;

@Service
public class TimesheetService {
	private TimesheetRepository timesheetRepository;
	private UserService userService;

	public TimesheetService(TimesheetRepository timesheetRepository, UserService userService) {
		super();
		this.timesheetRepository = timesheetRepository;
		this.userService = userService;
	}
	
	public List<String> dateRangeExtractor(LocalDate startDate, LocalDate endDate){
        List<String> dateList = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            dateList.add(startDate.toString());
            startDate = startDate.plusDays(1);
        }
        return dateList;
	}

	public List<String> detectTimesheetDuplicatedDates(Principal principal) {
		Long userId = userService.getCurrentUserId(userService.getCurrentUsername(principal));
		List<Timesheet> timesheets = timesheetRepository.findByUserId(userId);
		
		Set<String> temp = new HashSet<>();
		Set<String> duplicatedDates = new HashSet<>();
		for(Timesheet timesheet : timesheets) {
			List<String> dateRange = dateRangeExtractor(timesheet.getStartDate(), timesheet.getEndDate());
			for(String date : dateRange) {
				if(!temp.add(date)) {
					duplicatedDates.add(date);
				}
			}
		}
		return new ArrayList<>(duplicatedDates);
	}
	
	public Map<String, Set<String>> detectDuplicatedTimesheetDatesAdmin(){
		List<Timesheet> timesheets = timesheetRepository.findAll();
		Map<String, List<Timesheet>> id_timesheet = new HashMap<String, List<Timesheet>>();
		for(Timesheet t : timesheets) {
			String username = t.getUser().getUsername();
			if(!id_timesheet.containsKey(username)) {
				id_timesheet.put(username, new ArrayList<>());
			}
			id_timesheet.get(username).add(t);
		}
		
		Map<String, Set<String>> duplicatedDatesByUser = new HashMap<>();
		
		for(Map.Entry<String, List<Timesheet>> entry : id_timesheet.entrySet()) {
			String username = entry.getKey();
			List<Timesheet> userTimesheets = entry.getValue();
			
			Set<String> temp = new HashSet<>();
			Set<String> duplicatedDates = new HashSet<>();
			for(Timesheet timesheet : userTimesheets) {
				List<String> dateRange = dateRangeExtractor(timesheet.getStartDate(), timesheet.getEndDate());
				for(String date : dateRange) {
					if(!temp.add(date)) {
						duplicatedDates.add(date);
					}
				}
			}
			if (!duplicatedDates.isEmpty()) {
		        duplicatedDatesByUser.put(username, duplicatedDates);
		    }
			System.out.println(duplicatedDatesByUser);
		}
		return duplicatedDatesByUser;
	}
}
