package com.example.users.beans.timesheetbeans;

import com.example.users.beans.userbeans.WebUser;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DaysOff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String date;
	private boolean isApproved;
	private boolean isConsumed;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private WebUser user;
	
	public DaysOff() {
		
	}
	
	public DaysOff(Long id, String date, boolean isApproved, boolean isConsumed, WebUser user) {
		super();
		this.id = id;
		this.date = date;
		this.isApproved = isApproved;
		this.isConsumed = isConsumed;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public boolean isConsumed() {
		return isConsumed;
	}

	public WebUser getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public void setConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public void setUser(WebUser user) {
		this.user = user;
	}
}
