package org.kammerer_neuhold;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ServerEndpoint extends TextWebSocketHandler {
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("New WebSocket connection: " + session.getId());
        session.sendMessage(new TextMessage("Connected to WebSocket Server!"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Message received from " + session.getId() + ": " + message.getPayload());
        broadcast("User " + session.getId() + ": " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
        broadcast("User " + session.getId() + " disconnected.");
    }

    private void broadcast(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}