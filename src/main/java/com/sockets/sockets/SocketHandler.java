package com.sockets.sockets;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sockets.sockets.entity.Message;
import com.sockets.sockets.entity.User;
import com.sockets.sockets.enumerate.MessageType;

@Component
public class SocketHandler extends TextWebSocketHandler {

	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	private Logger logger = Logger.getLogger(WebSocketHandler.class.getName());

	private Map<User, WebSocketSession> userSessions = new ConcurrentHashMap<>();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException,
			IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		logger.info("Sending message: " + message.getPayload() + " to " + userSessions.size() + " sessions.");
		String payload = message.getPayload();
		ObjectMapper mapper = new ObjectMapper();
		Object obb = mapper.readValue(payload, Object.class);
		Field[] fields = obb.getClass().getDeclaredFields();
		String fi = obb.toString();
		String type = getTypeValue(obb.toString());
		if (type.equals(MessageType.JOINED.toString()) || type.equals(MessageType.MESSAGE.toString())
				|| type.equals(MessageType.TYPING.toString()) || type.equals(MessageType.LEFT.toString())) {
			Message obj = mapper.readValue(payload, Message.class);
			User user = new User(obj.getFrom(), obj.getFromUserName(), true);
			if (obj.getType().equalsIgnoreCase(MessageType.JOINED.toString())) {
				logger.info(user.getUserName() + " Joined the chat");
				userSessions.put(user, session);
			} else if (obj.getType().equalsIgnoreCase(MessageType.LEFT.toString())) {
				logger.info(user.getUserName() + " Left the chat");
				userSessions.remove(user);
			}
		}

		if (type.equals(MessageType.JOINED.toString()) || type.equals(MessageType.MESSAGE.toString())
				|| type.equals(MessageType.TYPING.toString()) || type.equals(MessageType.LEFT.toString())) {
			for (WebSocketSession webSocketSession : userSessions.values()) {

				webSocketSession.sendMessage(message);

			}
		} else {
			for (WebSocketSession webSocketSession : sessions) {
				if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
					webSocketSession.sendMessage(message);
				}
			}
		}
	}

	public static String getTypeValue(String input) {
		// Utilizamos una expresión regular para buscar el valor de 'type'
		Pattern pattern = Pattern.compile("type=([^,}]+)");
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			// Obtenemos el valor encontrado y eliminamos espacios en blanco al principio y
			// al final
			String type = matcher.group(1).trim();
			return type;
		} else {
			// Si no se encuentra, devolvemos el string original sin cambios
			return input.trim();
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info("Removed Websocket session, total number of sessions are " + userSessions.size());
	}

	public Set<User> getOnlineUsers() {
		return userSessions.keySet();
	}
}