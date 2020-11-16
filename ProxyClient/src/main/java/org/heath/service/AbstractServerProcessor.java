package org.heath.service;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
@Log4j2
public abstract class AbstractServerProcessor implements IServerProcessor {
    private ArrayList<IListener> listeners = new ArrayList<>();

    //处理对接对接消息
    protected void handleDock(String dockId) {
        log.info("处理对接消息：dockId=" + dockId);
    }

    //处理指令消息
    protected void handleCMD(String cmd) {
        log.info("处理指令消息：cmd=" + cmd);
    }

    @Override
    public void attach(IListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyAllLis() {
        for (IListener listener : listeners) {
            listener.update();
        }
    }
}
