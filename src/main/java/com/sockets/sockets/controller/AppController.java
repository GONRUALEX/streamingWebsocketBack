package com.sockets.sockets.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;

import com.sockets.sockets.SocketHandler;
import com.sockets.sockets.entity.LoginRequest;
import com.sockets.sockets.entity.User;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppController {

	@Autowired
	private SocketHandler webSocketHandler;

	private List<User> validUsers = new ArrayList<>();
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public User userLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		Optional<User> user = getValidUsers()
								.stream()
								.filter(u -> u.getUserName().equalsIgnoreCase(loginRequest.getName()))
								.findFirst();
		
		if (user.isPresent()) {
			return user.get();
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public List<User> listUsers() {
		List<User> validUsers = getValidUsers();
		Set<User> onlineUsers = webSocketHandler.getOnlineUsers();
		validUsers.forEach(validUser -> {
			if (onlineUsers.contains(validUser)) {
				validUser.setOnline(true);
			} else {
				validUser.setOnline(false);
			}
		});
		return validUsers;
	}
	
	private List<User> getValidUsers() {
		if (!validUsers.isEmpty()) {
			return validUsers;
		} else {
			validUsers.add(new User(1, "Frodo", false));
			validUsers.add(new User(2, "Samwise", false));
			validUsers.add(new User(3, "Marry", false));
			validUsers.add(new User(4, "Pippin", false));
			validUsers.add(new User(5, "Gollum", false));
			validUsers.add(new User(6, "Gandalf", false));
			validUsers.add(new User(7, "Aragorn", false));
			validUsers.add(new User(8, "Boromir", false));
			validUsers.add(new User(9, "Legolas", false));
			validUsers.add(new User(10, "Gimli", false));
			return validUsers;
		}
	}

}
