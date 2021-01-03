package wjt.service.impl;

import org.springframework.stereotype.Service;
import wjt.service.CandidateService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class CandidateServiceImpl implements CandidateService {

    private final Map<String,String> idMapCandidate=new ConcurrentHashMap<>();

    @Override
    public void saveCandidate(String userId, String candidate) {
        idMapCandidate.put(userId,candidate);
    }

    @Override
    public String getCandidate(String userId) {
        return idMapCandidate.get(userId);
    }
}
