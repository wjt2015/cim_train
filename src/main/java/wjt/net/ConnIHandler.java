package wjt.net;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnIHandler {

    private static final String ATTR_KEY = "clientInfo";

    /**
     * client_uid->channel;
     */
    private static final Map<Long, NioSocketChannel> UID_MAP_CHANNEL = new ConcurrentHashMap<>();


    public static NioSocketChannel getChannelByUid(final long uid) {
        return UID_MAP_CHANNEL.get(uid);
    }

    public static void addChannel(final long uid, final NioSocketChannel channel) {
        UID_MAP_CHANNEL.put(uid, channel);
    }

    public static void deleteChannel(final long uid) {
        UID_MAP_CHANNEL.remove(uid);
    }

    public static void addChannel(final long uid, final NioSocketChannel channel, final Object obj) {
        UID_MAP_CHANNEL.put(uid, channel);
        channel.attr(AttributeKey.newInstance(ATTR_KEY)).set(obj);
    }

}
