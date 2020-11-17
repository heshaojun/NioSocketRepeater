package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.service.AbstractMsgServer;
import org.heath.service.IChannelRegister;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class MsgServerRunner extends AbstractMsgServer implements IRunner {
    public MsgServerRunner(IChannelRegister channelRegister) {
        super(channelRegister);
    }

    @Override
    public void boot() {
        log.info("启动消息服务器启动线程");
        new Thread(this).start();
    }
}
