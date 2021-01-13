package wjt.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface UserService {
    void userEnter(final WebSocketSession webSocketSession);

    void userExit(final WebSocketSession webSocketSession);

    List<WebSocketSession> getAllUsers();

    WebSocketSession getUserByUid(final String userId);
}


