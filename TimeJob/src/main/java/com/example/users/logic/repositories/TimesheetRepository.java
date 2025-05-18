package com.example.users.logic.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.users.beans.timesheetbeans.Timesheet;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long>{
	
	List<Timesheet> findByUserId (Long user_id);
	Optional<Timesheet> findById (Long id);
	
}
