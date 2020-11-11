package org.heath.service;

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
public class ServerMsgHandlerStarter {
    ServerMsgHandler handler = new ServerMsgHandler();

    public void startup() {
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
                }
            }
        }).start();
    }

    private void handleData(byte[] data) {
        Hashtable<String, String> dataMap = MsgPackageUtils.unpackData(data);
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
