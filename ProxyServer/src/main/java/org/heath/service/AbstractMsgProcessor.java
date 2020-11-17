package org.heath.service;

import lombok.extern.log4j.Log4j2;
import org.heath.utils.MsgPackUtils;

import java.util.Map;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public abstract class AbstractMsgProcessor implements IClientMsgProcessor {
    @Override
    public void handleMsg(byte[] data) {
        Map<String, String> map = MsgPackUtils.unpack(data);
        log.info("接收到客户端消息：" + map);
    }
}
