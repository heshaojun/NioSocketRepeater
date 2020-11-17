package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.service.AbstractMsgServer;
import org.heath.service.IChannelSelector;

import java.nio.channels.SocketChannel;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class DefaultMsgServer extends AbstractMsgServer {

    public DefaultMsgServer(IChannelSelector channelRegister) {
        super(channelRegister);
    }

    @Override
    protected boolean auth(SocketChannel channel) {
        return true;
    }
}
