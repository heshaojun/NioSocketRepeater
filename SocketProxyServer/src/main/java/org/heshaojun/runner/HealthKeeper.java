package org.heshaojun.runner;

import org.heshaojun.common.CommonConst;
import org.heshaojun.service.IRunner;

import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * @author heshaojun
 * @date 2020/11/20
 * @description TODO
 */
public class HealthKeeper implements IRunner {
    private long msgClientTimeout;
    private long cachedChannelTimeout;
    private long mappedChannelTimeout;

    public HealthKeeper() {
        this.msgClientTimeout = Long.valueOf(System.getProperty("msg.client.timeout", "" + (30 * 1000)));
        this.cachedChannelTimeout = Long.valueOf(System.getProperty("cached.channel.timeout", "" + (10 * 1000)));
        this.mappedChannelTimeout = Long.valueOf(System.getProperty("mapped.channel.timeout", "" + (30 * 1000)));
    }

    @Override
    public void boot() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                checkMsgClient();
                checkCachedChannel();
                checkMappedChannel();
            }
        }, 100, 5 * 1000);
    }

    private void checkMsgClient() {
        try {
            List<CommonConst.MsgClientInfo> items = new ArrayList<>();
            for (Map.Entry<SocketChannel, CommonConst.MsgClientInfo> entry : CommonConst.MSG_CLIENT_INFO_MAP.entrySet()) {
                if (entry.getValue().ifTimeout(cachedChannelTimeout)) {
                    items.add(entry.getValue());
                }
            }
            for (CommonConst.MsgClientInfo item : items) {
                try {
                    item.destroy();
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
        }
    }

    private void checkCachedChannel() {
        try {
            List<CommonConst.CachedChannelInfo> items = new ArrayList<>();
            for (Map.Entry<String, CommonConst.CachedChannelInfo> entry : CommonConst.CACHED_CHANNEL_INFO_MAP.entrySet()) {
                if (entry.getValue().ifTimeout(msgClientTimeout)) {
                    items.add(entry.getValue());
                }
            }
            for (CommonConst.CachedChannelInfo item : items) {
                try {
                    item.destroy();
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
        }
    }

    private void checkMappedChannel() {
        try {
            List<CommonConst.ChannelInfo> items = new ArrayList<>();
            for (Map.Entry<SocketChannel, CommonConst.ChannelInfo> entry : CommonConst.MAPPED_CHANNEL.entrySet()) {
                if (entry.getValue().ifTimeout(mappedChannelTimeout)) {
                    items.add(entry.getValue());
                }
            }
            for (CommonConst.ChannelInfo item : items) {
                try {
                    item.destroy();
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
        }
    }
}
