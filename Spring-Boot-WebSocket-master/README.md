# Web Socket Chat Application

 * @author Valdeta Demiri
 * Created: 2025
 * Programming Language: Java
 * IDE: IntelliJ

# Prerequisites

- Java 17+ installed
- Spring Boot & Maven installed
- MySQL database set up
- Postman (optional for testing the Web Socket)
- SockJS & STOMP (for WebSocket communication)

# About the Project

WebSocket Chat Application is a real-time chat system using Spring Boot WebSockets and STOMP protocol. It allows users to join chat rooms, send messages instantly, and retrieve chat history.

# Key Features

✅ Real-time WebSocket chat
✅ STOMP-based messaging system
✅ Multiple chat rooms support
✅ Chat history stored in MySQL
✅ User join/leave notifications
✅ Frontend built with HTML, CSS, and JavaSc


# API Features

- Product API - Create, Search, Delete products
- Stock API - Add & Export stock data
- Purchase API - Export purchase data as PDF
- User Authentication - Secure login & registration

# How it Works

* Run the application → Available at http://localhost:8080/
* Users enter a username & chat ID to join a chat room.
* Messages are exchanged in real-time using WebSockets.
* Frontend uses SockJS & STOMP.js for WebSocket communication.

# Project Structure

src/main/java/com/javatechie/spring/ws/api/
│── config/
│   ├── WsConfig.java  -> WebSocket configuration class
│── controller/
│   ├── ChatController.java -> Handles WebSocket messages & user registration
│── model/
│   ├── ChatMessage.java -> Entity representing a chat message
│── repository/
│   ├── ChatMessageRepository.java -> JPA repository for chat messages
│── resources/
│   ├── static/ -> Frontend (HTML, CSS, JavaScript)
│── application.yml -> Database and server configurations


# Screenshots
