package com.example.users.logic.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.users.beans.timesheetbeans.DaysOff;

@Repository
public interface DaysOffRepository extends JpaRepository<DaysOff, Long>{
	
	Optional<DaysOff> findById(Long id);
	Optional<DaysOff> findByDate(String date);
	List<DaysOff> findByUserId (Long user_id);
	Optional<DaysOff> findByDateAndUserId(String date, Long userId);

}
