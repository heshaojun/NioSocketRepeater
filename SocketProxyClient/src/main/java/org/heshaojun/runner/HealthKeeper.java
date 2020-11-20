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

    private long mappedChannelTimeout;

    public HealthKeeper() {
        this.mappedChannelTimeout = Long.valueOf(System.getProperty("mapped.channel.timeout", "" + (30 * 1000)));
    }

    @Override
    public void boot() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                checkMappedChannel();
            }
        }, 100, 3 * 1000);
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
