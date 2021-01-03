package wjt.service.impl;

import org.springframework.stereotype.Service;
import wjt.service.SdpService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class SdpServiceImpl implements SdpService {

    private final Map<String,String> idMapOffser=new ConcurrentHashMap<>();
    private final Map<String,String> idMapAnswer=new ConcurrentHashMap<>();

    @Override
    public void saveOffer(String userId, String offer) {
        idMapOffser.put(userId,offer);
    }

    @Override
    public String getOffer(String userId) {
        return idMapOffser.get(userId);
    }

    @Override
    public void saveAnswer(String userId, String answer) {
        idMapAnswer.put(userId,answer);
    }

    @Override
    public String getAnswer(String userId) {
        return idMapAnswer.get(userId);
    }
}
