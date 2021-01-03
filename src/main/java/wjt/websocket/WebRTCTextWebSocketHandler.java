package wjt.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import wjt.service.CandidateService;
import wjt.service.SdpService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebRTCTextWebSocketHandler extends TextWebSocketHandler {

    private final Map<String,WebSocketSession> idMapSession=new ConcurrentHashMap<>();

    @Resource
    private SdpService sdpService;

    @Resource
    private CandidateService candidateService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        idMapSession.put(session.getId(),session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(message.getPayload());
        String type = jsonObject.getString("type");
        JSONObject jsonData = jsonObject.getJSONObject("data");
        if(type.equals("_send_offer")){

            String offer=jsonData.toJSONString();
            sdpService.saveOffer(session.getId(),offer);
        }else if(type.equals("_send_candidate")){

            String candidate=jsonData.toJSONString();
            candidateService.saveCandidate(session.getId(),candidate);
        }else if(type.equals("_get_v_list")){

            UserListInfo userListInfo = new UserListInfo();
            userListInfo.data= Lists.newArrayList(idMapSession.keySet());
            String result=JSON.toJSONString(userListInfo);
            session.sendMessage(new TextMessage(result));

        }else if(type.equals("_get_other_candidate")){

            String otherUserId=jsonData.toJSONString();
            String otherCandidate = candidateService.getCandidate(otherUserId);
            CandidateInfo candidateInfo = new CandidateInfo();
            candidateInfo.data=otherCandidate;

            session.sendMessage(new TextMessage(JSON.toJSONString(candidateInfo)));

        }else if(type.equals("_get_other_offer")){

        }else if(type.equals("_get_other_answer")){

        }


    }

    static class UserListInfo{
        public final String type="_get_v_list";
        public List<String> data;
    }

    static class CandidateInfo{
        public final String type="_get_other_candidate";
        public String data;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("session={};e={};",session,exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        idMapSession.remove(session.getId());
    }

}
