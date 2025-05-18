package com.example.users.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.users.beans.MessageBean;
import com.example.users.beans.userbeans.WebUser;
import com.example.users.logic.repositories.MessageRepository;
import com.example.users.logic.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MessageController {
	
	private MessageRepository messageRepository;
	private UserService userService;
	
	public MessageController(MessageRepository messageRepository, UserService userService) {
		super();
		this.messageRepository = messageRepository;
		this.userService = userService;
	}

	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	@PostMapping("/contact-succes")
    public String handleContact(@RequestParam String message, Principal principal, HttpSession session) {
		WebUser currentUser = userService.findWebUser(userService.getCurrentUsername(principal));
		MessageBean messageBean = new MessageBean();
		messageBean.setMessage(message);
		messageBean.setUser(currentUser);
		messageBean.setTreated(false);
		messageRepository.save(messageBean);
		
		session.setAttribute("messageSent", true);
		return "redirect:/contact-succes";
    }
	
	@GetMapping("/contact-succes")
	public String contactSucces(HttpSession session) {
		Object flag = session.getAttribute("messageSent");
		if (flag != null) {
			session.removeAttribute("messageSent");
			return "contact-succes";
		}
		return "redirect:/contact";
	}
	
	@GetMapping("/admin/messages")
	public String getMessages(Model model) {
		List<MessageBean> messages = messageRepository.findAll();
		model.addAttribute("messages", messages);
		return "admin-messages";
	}
	
	@PostMapping("/treat-message")
	public String treatMessage(@RequestParam("id") Long id) {
		Optional<MessageBean> optional_message = messageRepository.findById(id);
		if(optional_message.isPresent()) {
			MessageBean message = optional_message.get();
			message.setTreated(true);
			messageRepository.save(message);
		}
		return "redirect:/admin/messages";
	}
	
	@PostMapping("/untreat-message")
	public String untreatMessage(@RequestParam("id") Long id) {
		Optional<MessageBean> optional_message = messageRepository.findById(id);
		if(optional_message.isPresent()) {
			MessageBean message = optional_message.get();
			message.setTreated(false);
			messageRepository.save(message);
		}
		return "redirect:/admin/messages";
	}
}
