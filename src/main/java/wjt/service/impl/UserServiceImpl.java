package wjt.service.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import wjt.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private Map<String, WebSocketSession> uidMapSession = new ConcurrentHashMap<>();

    @Override
    public void userEnter(WebSocketSession webSocketSession) {
        uidMapSession.put(webSocketSession.getId(), webSocketSession);
    }

    @Override
    public void userExit(WebSocketSession webSocketSession) {
        uidMapSession.remove(webSocketSession.getId());
    }

    @Override
    public List<WebSocketSession> getAllUsers() {
        return Lists.newArrayList(uidMapSession.values());
    }

    @Override
    public WebSocketSession getUserByUid(String userId) {
        return uidMapSession.get(userId);
    }
}
