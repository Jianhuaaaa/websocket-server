package com.jsun.websocket.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EchoWebSocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session.getAttributes().toString());
        String key = session.getAttributes().get("wsId").toString();
        if (sessions.containsKey(key)) {
            WebSocketSession previousSession = sessions.get(key);
            if (previousSession.isOpen()) {
                previousSession.close(CloseStatus.SERVICE_RESTARTED);
            }
            sessions.remove(key);
            log.info("Closed and renew connection {}", key);
        }
        log.info("New connection {} created successfully", key);
        sessions.put(key, session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Received message from client");
        if (message instanceof PongMessage) {
            session.sendMessage(new PongMessage(ByteBuffer.wrap("2".getBytes())));
        } else if (message instanceof TextMessage) {
            log.info("Received TextMessage");
        } else if (message instanceof BinaryMessage) {
            log.info("Received BinaryMessage");
        } else {
            throw new UnsupportedOperationException("Message format is not supported");
        }
        log.info("Session ID={}, message={}", session.getId(), message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.warn("Connection Closed. CloseCode={}, CloseReason={}", closeStatus.getCode(), closeStatus.getReason());
        String key = session.getAttributes().get("wsId").toString();
        log.info(key);
        sessions.remove(key);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * Check connection status
     *
     * @param key
     * @return
     */
    public boolean isConnected(String key) {
        if (sessions.containsKey(key)) {
            WebSocketSession session = sessions.get(key);
            return null != session && session.isOpen();
        }
        return false;
    }

    public void sendMessage(String key, String message) throws IOException {
        log.info("Sending message: ", message);
        if (!isConnected(key)) {
            log.error("Failed to send message. Connection {} interrupted. ", key);
            // TODO. Add error handling logic here
        }
        // send message
        sessions.get(key).sendMessage(new TextMessage(message));
    }
}
