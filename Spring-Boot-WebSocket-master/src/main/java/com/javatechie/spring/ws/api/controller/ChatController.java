package com.javatechie.spring.ws.api.controller;

import com.javatechie.spring.ws.api.model.ChatMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class ChatController {

	private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> activeChats = new ConcurrentHashMap<>();

	@MessageMapping("/chat/{chatId}/register")
	@SendTo("/topic/{chatId}")
	public ChatMessage registerUserToChat(
			@Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String chatId) {

		String username = chatMessage.getSender();

		synchronized (activeChats) {
			if (!activeChats.containsKey(chatId) && !activeChats.isEmpty()) {
				throw new IllegalArgumentException("Chat ID mismatch. Cannot join this chat.");
			}

			activeChats.putIfAbsent(chatId, new CopyOnWriteArrayList<>());
			CopyOnWriteArrayList<String> users = activeChats.get(chatId);

			if (!users.contains(username)) {
				users.add(username);
			}

			headerAccessor.getSessionAttributes().put("username", username);
			headerAccessor.getSessionAttributes().put("chatId", chatId);

			chatMessage.setType(ChatMessage.MessageType.JOIN);
			return chatMessage;
		}
	}

	@MessageMapping("/chat/{chatId}/send")
	@SendTo("/topic/{chatId}")
	public ChatMessage sendMessageToChat(
			@Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String chatId) {
		String sessionChatId = (String) headerAccessor.getSessionAttributes().get("chatId");
		if (!chatId.equals(sessionChatId)) {
			throw new IllegalArgumentException("Chat ID mismatch. Cannot send messages.");
		}

		return chatMessage;
	}

	@MessageExceptionHandler(IllegalArgumentException.class)
	public void handleChatError(IllegalArgumentException ex) {
		System.err.println("Error: " + ex.getMessage());
	}
}
