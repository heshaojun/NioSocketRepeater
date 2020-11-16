package org.heath.service;

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
        
    }
}
