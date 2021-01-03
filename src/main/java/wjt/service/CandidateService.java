package wjt.service;

public interface CandidateService {
    void saveCandidate(final String userId,final String candidate);
    String getCandidate(final String userId);
}
