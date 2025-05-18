package com.example.users.logic.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.users.beans.MessageBean;

@Repository
public interface MessageRepository extends JpaRepository<MessageBean, Long>{
	Optional<MessageBean> findById(Long id);
}
