package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;

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
    @Override
    public void boot() {
        new Thread(() -> {
            log.info("启动对接信息处理器线程");
        }).start();
    }

    public void handler() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(CommonProperties.PACK_SIZE);
        Map<String, String> map = new Hashtable<>();
        map.put(CommonConst.TOKEN, CommonConst.DOCK_TYPE);
        while (true) {
            try {
                if (CommonConst.MSG_CLIENT_INFO_MAP.isEmpty()) {
                    Thread.sleep(100);
                    continue;
                }
                String dockerId = CommonConst.DOCK_MSG_QUEUE.take();
                map.put(CommonConst.DOCK, dockerId);
                int index = dockerId.hashCode() % CommonConst.MSG_CLIENT_INFO_MAP.size();
                CommonConst.MSG_CLIENT_INFO_MAP.entrySet();
                Map.Entry<SocketChannel, CommonConst.MsgClientInfo> entry = (Map.Entry<SocketChannel, CommonConst.MsgClientInfo>) CommonConst.MSG_CLIENT_INFO_MAP.entrySet().toArray()[index];
                CommonConst.MsgClientInfo clientInfo = entry.getValue();
                SocketChannel channel = clientInfo.getChannel();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
