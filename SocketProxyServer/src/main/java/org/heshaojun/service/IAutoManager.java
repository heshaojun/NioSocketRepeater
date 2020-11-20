package org.heshaojun.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public interface IAutoManager {

    boolean isAlive();

    void shutdown();

    boolean isWorking();

    void stopWork();

    void startup();


    long getPeriod();

    default void boot() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isAlive() && !isWorking()) {
                    startup();
                }
            }
        }, 100, getPeriod());
    }
}
