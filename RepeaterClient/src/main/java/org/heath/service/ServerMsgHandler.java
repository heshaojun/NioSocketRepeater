package org.heath.service;

import lombok.extern.log4j.Log4j2;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
@Log4j2
public class ServerMsgHandler {


    public void handleDock(String id) {
        log.debug("开始进行对接处理，对接到id：" + id);
    }

    public void handleCMD(String command) {
    }
}
