package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.common.CommonStatus;
import org.heath.utils.MsgPackageUtils;

import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
@Log4j2
public class ServerMsgHandlerStarter {
    private static ServerMsgHandler handler = new ServerMsgHandler();

    public static void startup() {
        log.info("启动服务器消息处理器启动线程");
        new Thread(() -> {
            while (true) {
                try {
                    byte[] data = CommonConst.SERVER_MSG_QUEUE.take();
                    if (data != null) {
                        CompletableFuture.runAsync(() -> {
                            handleData(data);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("读取服务器消息队列中到消息异常", e);
                }
            }
        }).start();
    }

    private static void handleData(byte[] data) {
        Hashtable<String, String> dataMap = MsgPackageUtils.unpackData(data);
        log.debug("读取到来自服务器的消息：" + dataMap.toString());
        if (dataMap == null) {
            CommonStatus.msgClientStatus = CommonStatus.MsgClientStatus.BAD_DATA;
            return;
        }
        if (!CommonProperties.msgServerId.equals(dataMap.get(CommonConst.SERVER_ID))) {
            CommonStatus.msgClientStatus = CommonStatus.MsgClientStatus.BAD_DATA;
            return;
        }
        switch (dataMap.get(CommonConst.TYPE)) {
            case CommonConst.DOCK_TYPE:
                log.debug("读取到到消息为对接类型消息");
                try {
                    String id = dataMap.get(CommonConst.DOCK);
                    if (id == null || id == "") {
                        CommonStatus.msgClientStatus = CommonStatus.MsgClientStatus.BAD_DATA;
                        return;
                    }
                    handler.handleDock(id);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonStatus.msgClientStatus = CommonStatus.MsgClientStatus.BAD_DATA;
                    return;
                }
                break;
            default:
                break;
        }
    }
}
