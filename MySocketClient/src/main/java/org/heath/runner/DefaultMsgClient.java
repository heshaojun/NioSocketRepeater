package org.heath.runner;

import lombok.extern.log4j.Log4j2;
import org.heath.service.AbstractMsgClient;
import org.heath.service.IRunner;

import java.nio.channels.SocketChannel;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
@Log4j2
public class DefaultMsgClient extends AbstractMsgClient {

    @Override
    protected boolean auth(SocketChannel channel) {
        return true;
    }
}
