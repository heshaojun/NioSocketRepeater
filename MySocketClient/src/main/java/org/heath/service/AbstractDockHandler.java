package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.utils.MsgPackUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description TODO
 */
@Log4j2
public abstract class AbstractDockHandler implements IRunner {
    protected IChannelSelector channelSelector;

    public AbstractDockHandler(IChannelSelector channelSelector) {
        this.channelSelector = channelSelector;
    }

    @Override
    public void boot() {
        new Thread(() -> {
            log.info("启动对接信息处理器线程");
            handler();
        }).start();
    }

    public void handler() {
        Map<String, String> map = new Hashtable<>();
        map.put(CommonConst.TYPE, CommonConst.DOCK_TYPE);
        while (true) {
            try {
                String dockerId = CommonConst.DOCK_MSG_QUEUE.take();
                map.put(CommonConst.DOCK, dockerId);
                byte[] data = MsgPackUtils.pack(map, "DOCK_AUTH", CommonProperties.PACK_SIZE);
                dock(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected abstract void dock(byte[] data);

}
