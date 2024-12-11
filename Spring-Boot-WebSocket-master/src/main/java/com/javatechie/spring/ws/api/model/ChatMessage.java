package com.javatechie.spring.ws.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_chat_message")
public class ChatMessage {
	@Id
	@GeneratedValue
	private Long id;
	private String content;
	private String sender;

	public enum MessageType {
		CHAT, LEAVE, JOIN
	}

	private MessageType type;
}
