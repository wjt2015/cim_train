package wjt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wjt.service.SessionService;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    private Map<String, HttpSession> idMapSession = new ConcurrentHashMap<>();

    @Override
    public HttpSession getSessionById(String sid) {
        return idMapSession.get(sid);
    }

    @Override
    public void saveSession(String sid, HttpSession session) {
        idMapSession.put(sid, session);
    }
}


