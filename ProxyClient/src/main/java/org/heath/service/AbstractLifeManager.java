package org.heath.service;

/**
 * @author heshaojun
 * @date 2020/11/16
 * @description TODO
 */
public abstract class AbstractLifeManager implements ILifeManager {
    private volatile boolean alive = false;
    private volatile boolean work = false;

    @Override
    public boolean isWorking() {
        return work;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void workup() {
        work = true;
    }

    @Override
    public void hangup() {
        work = false;
    }

    @Override
    public void shutdown() {
        alive = false;
    }

    @Override
    public void startup() {
        alive = true;
    }
}
