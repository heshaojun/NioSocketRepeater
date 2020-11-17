package org.heath.service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public abstract class AbstractAutoManager implements IRunner, Runnable {
    private volatile boolean alive = false;
    private volatile boolean working = false;

    protected boolean isAlive() {
        return alive;
    }

    protected void stopLife() {
        alive = false;
    }

    protected boolean isWorking() {
        return working;
    }

    protected void stopWork() {
        working = false;
    }

    protected void startWork() {
        working = true;
    }

    protected void startLife() {
        alive = true;
    }

    protected long getPeriod() {
        return 20000L;
    }

    @Override
    public void boot() {
        Runnable runnable = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isWorking() && !isAlive()) {
                    new Thread(runnable).start();
                }
            }
        }, 100, getPeriod());
    }
}
