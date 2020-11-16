package org.heath.service;

import org.heath.common.CommonConst;
import org.heath.utils.MsgPackUtils;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public class ServerMsgProcessor extends AbstractServerMsgProcessor {
    @Override
    public void handle(byte[] data) {
        Map<String, String> dataMap = MsgPackUtils.unpack(data);
        if (dataMap == null) {
            notifyAllLis();
            return;
        }
        String type = dataMap.get(CommonConst.TYPE);
        if (type == null) {
            notifyAllLis();
            return;
        }
        switch (type) {
            case CommonConst.DOCK_TYPE:
                String id = dataMap.get(CommonConst.DOCK);
                if (id == null || "".equals(id)) {
                    notifyAllLis();
                    return;
                }
                handleDock(id);
                break;
            case CommonConst.CMD_TYPE:
                String cmd = dataMap.get(CommonConst.CMD);
                if (cmd == null || "".equals(cmd)) {
                    notifyAllLis();
                    return;
                }
                break;
            default:
                notifyAllLis();
                return;
        }
    }
}
