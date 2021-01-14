package wjt.service;

import javax.servlet.http.HttpSession;

public interface SessionService {
    HttpSession getSessionById(final String sid);

    void saveSession(final String sid, HttpSession session);
}
