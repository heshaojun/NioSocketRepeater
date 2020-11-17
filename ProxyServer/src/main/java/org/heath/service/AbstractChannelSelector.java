package org.heath.service;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heshaojun
 * @date 2020/11/17
 * @description TODO
 */
public abstract class AbstractChannelSelector implements IChannelSelector {
    protected List<Selector> selectors = new ArrayList<>();

    @Override
    public void registry(SocketChannel channel, int event) {
        int index = 0;
        int location = 0;
        int min = Integer.MAX_VALUE;
        for (Selector selector : selectors) {
            int keySize = selector.keys().size();
            if (keySize < min) {
                min = keySize;
                location = index;
            }
            index++;
        }
        try {
            channel.register(selectors.get(location), event);
        } catch (Exception e) {
        }
    }


    public List<Selector> getSelectors() {
        return selectors;
    }

}
