package org.heath.service;

import org.heath.common.CommonStatus;

/**
 * @author heshaojun
 * @date 2020/11/9
 * @description TODO
 */
public interface IMsgClient extends Runnable {
    void startup();

    @Override
    default void run() {
        if (!CommonStatus.isMsgClientAlive) {
            startup();
        }
    }
}
