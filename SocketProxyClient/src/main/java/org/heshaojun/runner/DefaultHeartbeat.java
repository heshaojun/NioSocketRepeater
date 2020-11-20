package org.heshaojun.runner;

import org.heshaojun.common.CommonConst;
import org.heshaojun.common.CommonProperties;
import org.heshaojun.service.AbstractHeartbeat;
import org.heshaojun.utils.MsgPackUtils;

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
        map.put(CommonConst.TOKEN, CommonProperties.token);
        //byte[] data = MsgPackUtils.pack(map, CommonProperties.authId, CommonProperties.PACK_SIZE);
        if (CommonProperties.clientKey == null) return null;
        byte[] data = MsgPackUtils.secretPack(map, CommonProperties.authId, CommonProperties.PACK_SIZE, CommonProperties.clientKey);
        return data;
    }

    @Override
    protected long getPeriod() {
        return 5 * 1000;
    }
}
