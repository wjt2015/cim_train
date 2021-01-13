package wjt.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import wjt.service.CandidateService;
import wjt.service.SdpService;
import wjt.service.UserService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {
 * websocket模拟直播,(https://blog.csdn.net/qq_39364032/article/details/79744309);
 * websocket/video_play.html
 * websocket/video_record.html
 * }
 * {
 * https://blog.csdn.net/chenhande1990chenhan/article/details/72831782?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control
 * <p>
 * <p>
 * <p>
 * https://webrtc.org.cn/20180604_rtcpeerconnection/
 * http://www.voidcn.com/article/p-ywdhlyyo-bqs.html
 * http://www.voidcn.com/article/p-moignzoa-rv.html
 * http://blog.csdn.net/leecho571/article/details/8146525
 * ---
 * 20210111,
 * video_play.html
 * <p>
 * <p>
 * }
 */
@Slf4j
public class VideoHandler implements WebSocketHandler {
    private static final String JSON_TYPE = "type";
    private static final String OFFER_TYPE = "_offer";
    private static final String CANDIDATE_TYPE = "_candidate";

    private Map<String, WebSocketSession> uidMapSession;

    private Map<String, JSONObject> uidMapCandidateJson = new ConcurrentHashMap<>();
    private Map<String, JSONObject> uidMapOfferJson = new ConcurrentHashMap<>();

    /**
     * 每个用户的icecandidate;当有新用户加入时推送给新用户;
     */
    private Map<String, WebSocketMessage<?>> uidMapCandidate = new ConcurrentHashMap<>();

    private final AtomicLong id = new AtomicLong();


    public VideoHandler(Map<String, WebSocketSession> uidMapSession) {
        this.uidMapSession = uidMapSession;
    }


    @Resource
    private SdpService sdpService;

    @Resource
    private CandidateService candidateService;

    @Resource
    private UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        uidMapSession.put(session.getId(), session);
        log.info("userId={} join!;uidMapSession={};", session.getId(), uidMapSession);
        uidMapCandidate.forEach((uid, candidate) -> {
            try {
                session.sendMessage(candidate);
            } catch (IOException e) {
                log.error("sendMsg error!uid={};candidate={};", uid, candidate, e);
            }
        });

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            handleTextMessage(session, (TextMessage) (message));
        }
    }

    private void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        log.info("sessionId={};payload={};", session.getId(), payload);
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String type = jsonObject.getString(JSON_TYPE);
        String userId = session.getId();

        if (type.equals("send_offer")) {
            String offer = jsonObject.getString("data");
            sdpService.saveOffer(session.getId(), offer);

        } else if (type.equals("get_offer")) {
            String targetUidStr = jsonObject.getString("data");
            String offer = sdpService.getOffer(targetUidStr);

            JSONObject recvOfferJson = new JSONObject();
            recvOfferJson.put("type", "recv_offer");

            JSONObject offerJson = new JSONObject();
            offerJson.put("uid", targetUidStr);
            offerJson.put("offer", offer);
            recvOfferJson.put("data", offerJson);

            session.sendMessage(new TextMessage(recvOfferJson.toJSONString()));

        } else if (type.equals("send_answer")) {
            JSONObject data = jsonObject.getJSONObject("data");
            String targetUidStr = data.getString("target_uid");
            String answer = data.getString("answer");

            WebSocketSession webSocketSession = userService.getUserByUid(targetUidStr);
            if (webSocketSession != null && webSocketSession.isOpen()) {

                JSONObject answerJson = new JSONObject();
                answerJson.put("uid", userId);
                answerJson.put("answer", answer);

                JSONObject recvAnswerJson = new JSONObject();
                recvAnswerJson.put("type", "recv_answer");
                recvAnswerJson.put("data", answerJson);
                webSocketSession.sendMessage(new TextMessage(recvAnswerJson.toJSONString()));
            }
        } else if (type.equals("send_candidate")) {
            String candidate = jsonObject.getString("data");
            candidateService.saveCandidate(userId, candidate);
        } else if (type.equals("recv_candidate")) {
            String targetUidStr = jsonObject.getString("target_uid");
            String candidate = candidateService.getCandidate(targetUidStr);
            if (!(Strings.isNullOrEmpty(candidate) || session == null || !session.isOpen())) {

                JSONObject candidateJson = new JSONObject();
                candidateJson.put("uid", targetUidStr);
                candidateJson.put("candidate", candidate);

                JSONObject recvCandidateJson = new JSONObject();
                recvCandidateJson.put("type", "recv_candidate");
                recvCandidateJson.put("data", candidateJson);
                session.sendMessage(new TextMessage(recvCandidateJson.toJSONString()));

            }

        }
    }


/*    private void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        log.info("sessionId={};payload={};", session.getId(), payload);
        JSONObject jsonObject = JSONObject.parseObject(payload);
        String type = jsonObject.getString(JSON_TYPE);
        if (OFFER_TYPE.equals(type)) {
            uidMapOfferJson.put(session.getId(), jsonObject.getJSONObject("data"));
            log.info("uidMapOfferJson={};",uidMapOfferJson);
        } else if (CANDIDATE_TYPE.equals(type)) {
            uidMapCandidateJson.put(session.getId(), jsonObject.getJSONObject("data"));
            log.info("uidMapCandidateJson={};",uidMapCandidateJson);
        }
    }*/

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        uidMapSession.remove(session.getId());
        log.info("userId={} exit!closeStatus={};uidMapSession={};", session.getId(), closeStatus, uidMapSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
