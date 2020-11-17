package org.heath.runner;

import org.heath.common.CommonConst;
import org.heath.common.CommonProperties;
import org.heath.service.AbstractHeartbeat;
import org.heath.service.IRunner;
import org.heath.utils.MsgPackUtils;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public class DefaultHeartbeat extends AbstractHeartbeat {
    private Map<String, String> map;

    public DefaultHeartbeat() {
        this.map = new Hashtable<>();
        map.put(CommonConst.TYPE, CommonConst.HEARTBEAT_TYPE);
    }

    @Override
    protected byte[] createHeartbeat() {
        map.put(CommonConst.HEARTBEAT, "" + new Date().getTime());
        byte[] data = MsgPackUtils.pack(map, CommonProperties.clientId, CommonProperties.PACK_SIZE);
        return data;
    }

    @Override
    protected long getPeriod() {
        return 2 * 1000;
    }
}
