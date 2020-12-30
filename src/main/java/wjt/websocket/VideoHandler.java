package wjt.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 {
 websocket模拟直播,(https://blog.csdn.net/qq_39364032/article/details/79744309);
 websocket/video_play.html
 websocket/video_record.html
 }
 {
 https://blog.csdn.net/chenhande1990chenhan/article/details/72831782?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control



 https://webrtc.org.cn/20180604_rtcpeerconnection/
 http://www.voidcn.com/article/p-ywdhlyyo-bqs.html
 http://www.voidcn.com/article/p-moignzoa-rv.html
 http://blog.csdn.net/leecho571/article/details/8146525
 }

 */
@Slf4j

public class VideoHandler implements WebSocketHandler {

    private Map<String,WebSocketSession> uidMapSession;

    private final AtomicLong id=new AtomicLong();

    public VideoHandler(Map<String, WebSocketSession> uidMapSession) {
        this.uidMapSession = uidMapSession;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        uidMapSession.put(session.getId(),session);
        log.info("userId={} join!;uidMapSession={};",session.getId(),uidMapSession);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        log.info("id={};message={};",id.getAndIncrement(),message);

        if(message instanceof TextMessage){
            TextMessage textMessage=(TextMessage)message;
            log.info("textMessage={};payload={};",textMessage,textMessage.getPayload());
        }


        uidMapSession.forEach((uid,webSocketSession)->{
            if(webSocketSession.isOpen()&&!webSocketSession.equals(session)){
                try {
                    webSocketSession.sendMessage(message);
                } catch (IOException e) {
                    log.error("send to userId={} error!",uid,e);
                }
            }
        });


    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        uidMapSession.remove(session.getId());
        log.info("userId={} exit!closeStatus={};uidMapSession={};",session.getId(),closeStatus,uidMapSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
