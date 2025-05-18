package com.example.users.logic.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.users.beans.userbeans.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>{
	
	Optional<Roles> findByName(String name);
	
}
