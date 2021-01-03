package wjt.service;

/**
 * 管理用户的sdp,offer+answer;
 */
public interface SdpService {
    void saveOffer(final String userId,final String offer);
    String getOffer(final String userId);

    void saveAnswer(final String userId,final String answer);
    String getAnswer(final String userId);



}
