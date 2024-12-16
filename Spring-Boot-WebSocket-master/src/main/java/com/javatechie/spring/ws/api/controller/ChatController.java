package com.javatechie.spring.ws.api.controller;

import com.javatechie.spring.ws.api.model.ChatMessage;
import com.javatechie.spring.ws.api.repository.ChatMessageRepository;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class ChatController {

	private final ConcurrentHashMap<String, CopyOnWriteArrayList<ChatMessage>> chatHistory = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> activeChats = new ConcurrentHashMap<>();
	private final ChatMessageRepository chatMessageRepository;
	private final SimpMessagingTemplate messagingTemplate;

	public ChatController(ChatMessageRepository chatMessageRepository, SimpMessagingTemplate messagingTemplate) {
		this.chatMessageRepository = chatMessageRepository;
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping("/chat/{chatId}/register")
	public void registerUserToChat(
			@Payload ChatMessage chatMessage,
			SimpMessageHeaderAccessor headerAccessor,
			@DestinationVariable String chatId) {

		activeChats.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).addIfAbsent(chatMessage.getSender());
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		headerAccessor.getSessionAttributes().put("chatId", chatId);

		sendChatHistoryToUser(chatId, headerAccessor.getSessionId());

		chatMessage.setType(ChatMessage.MessageType.JOIN);
		messagingTemplate.convertAndSend("/topic/" + chatId, chatMessage);
	}

	@MessageMapping("/chat/{chatId}/send")
	public void sendMessageToChat(@Payload ChatMessage chatMessage, @DestinationVariable String chatId) {
		chatHistory.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).add(chatMessage);
		chatMessageRepository.save(chatMessage);

		messagingTemplate.convertAndSend("/topic/" + chatId, chatMessage);
	}

	private void sendChatHistoryToUser(String chatId, String sessionId) {
		if (chatHistory.containsKey(chatId)) {
			CopyOnWriteArrayList<ChatMessage> history = chatHistory.get(chatId);

			messagingTemplate.convertAndSendToUser(sessionId, "/queue/history", history);
		}
	}
}
