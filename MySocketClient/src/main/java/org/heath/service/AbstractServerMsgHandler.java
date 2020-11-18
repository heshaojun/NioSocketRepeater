package org.heath.service;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.utils.MsgPackUtils;

import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/18
 * @description TODO
 */
public abstract class AbstractServerMsgHandler implements IRunner {
    protected IChannelSelector channelSelector;
    protected AbstractAutoManager msgClientAutoManager;

    public AbstractServerMsgHandler(IChannelSelector channelSelector, AbstractAutoManager msgClientAutoManager) {
        this.channelSelector = channelSelector;
        this.msgClientAutoManager = msgClientAutoManager;
    }

    @Override
    public void boot() {
        new Thread(() -> {
            handler();
        }).start();
    }

    private void handler() {
        while (true) {
            try {
                byte[] data = CommonConst.SERVER_MSG_QUEUE.take();
                if (data == null) continue;
                if (!msgClientAutoManager.isWorking()) continue;
                try {
                    Map<String, String> map = MsgPackUtils.unpack(data);
                    if (map == null) throw new Exception("错误的数据包");
                    if (!CommonProperties.authId.equals(map.get(CommonConst.AUTH_ID))) throw new Exception("授权id不正确");
                    String type = map.get(CommonConst.TYPE);
                    switch (type) {
                        case CommonConst.DOCK_TYPE:

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msgClientAutoManager.stopWork();
                }
            } catch (Exception e) {
            }
        }
    }

    protected abstract void handleDock(String id);

    protected abstract void handleCMD(String cmd);
}
