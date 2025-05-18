package com.example.users.logic.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.users.beans.userbeans.WebUser;

@Repository
public interface UserRepository extends JpaRepository<WebUser, Long>{
	
	Optional<WebUser> findByUsername(String username);
	
	boolean existsByUsername(String username);
	
}
