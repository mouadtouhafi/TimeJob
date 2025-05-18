package com.example.users.beans.timesheetbeans;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.users.beans.userbeans.WebUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Timesheet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Select a valid date")
	@Column(name = "start_date", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate;

	@NotNull(message = "Select a valid date")
	@Column(name = "end_date", nullable = false)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate endDate;

	@Transient
	private Map<String, Integer> dailyHours = new HashMap<>();

	@Column(nullable = false)
	@NotBlank(message = "Select your task")
	private String task;

	@Column(name = "total_hours")
	private String totalHours;

	private String notes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private WebUser user;
	private boolean isApproved;
	private String mondayHours;
	private String tuesdayHours;
	private String wednesdayHours;
	private String thursdayHours;
	private String fridayHours;
	private String saturdayHours;
	private String sundayHours;

	public Timesheet() {
	}

	public Timesheet(Long id, @NotNull(message = "Select a valid date") LocalDate startDate,
			@NotNull(message = "Select a valid date") LocalDate endDate, Map<String, Integer> dailyHours,
			@NotBlank(message = "Select your task") String task, String totalHours, String notes, WebUser user,
			boolean isApproved, String mondayHours, String tuesdayHours, String wednesdayHours, String thursdayHours,
			String fridayHours, String saturdayHours, String sundayHours) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dailyHours = dailyHours;
		this.task = task;
		this.totalHours = totalHours;
		this.notes = notes;
		this.user = user;
		this.isApproved = isApproved;
		this.mondayHours = mondayHours;
		this.tuesdayHours = tuesdayHours;
		this.wednesdayHours = wednesdayHours;
		this.thursdayHours = thursdayHours;
		this.fridayHours = fridayHours;
		this.saturdayHours = saturdayHours;
		this.sundayHours = sundayHours;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public Map<String, Integer> getDailyHours() {
		return dailyHours;
	}

	public String getTask() {
		return task;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public String getNotes() {
		return notes;
	}

	public WebUser getUser() {
		return user;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public String getMondayHours() {
		return mondayHours;
	}

	public String getTuesdayHours() {
		return tuesdayHours;
	}

	public String getWednesdayHours() {
		return wednesdayHours;
	}

	public String getThursdayHours() {
		return thursdayHours;
	}

	public String getFridayHours() {
		return fridayHours;
	}

	public String getSaturdayHours() {
		return saturdayHours;
	}

	public String getSundayHours() {
		return sundayHours;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setDailyHours(Map<String, Integer> dailyHours) {
		this.dailyHours = dailyHours;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setUser(WebUser user) {
		this.user = user;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public void setMondayHours(String mondayHours) {
		this.mondayHours = mondayHours;
	}

	public void setTuesdayHours(String tuesdayHours) {
		this.tuesdayHours = tuesdayHours;
	}

	public void setWednesdayHours(String wednesdayHours) {
		this.wednesdayHours = wednesdayHours;
	}

	public void setThursdayHours(String thursdayHours) {
		this.thursdayHours = thursdayHours;
	}

	public void setFridayHours(String fridayHours) {
		this.fridayHours = fridayHours;
	}

	public void setSaturdayHours(String saturdayHours) {
		this.saturdayHours = saturdayHours;
	}

	public void setSundayHours(String sundayHours) {
		this.sundayHours = sundayHours;
	}
}
