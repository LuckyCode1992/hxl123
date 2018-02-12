package com.mysirui.vehicle.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Channel 监听容器
 * Created by marlin on 2017/5/23.
 */

public class ChannelEventListenerContainer<T> implements ChannelListener<T> {

    private List<ChannelListener> listenerList = new ArrayList<ChannelListener>();

    public ChannelEventListenerContainer add(ChannelListener listener){
        listenerList.add(listener);
        return this;
    }
    public ChannelEventListenerContainer addFirst(ChannelListener listener){
        listenerList.add(0,listener);
        return this;
    }
    public ChannelEventListenerContainer remove(ChannelListener listener){
        listenerList.remove(listener);
        return this;
    }

    @Override
    public void onReceive(T receiveMsg) {
        for(ChannelListener listener:listenerList){
            listener.onReceive(receiveMsg);
        }
    }

    @Override
    public void willSend(T sendMsg) {

    }

    @Override
    public void onConnect() {
        for(ChannelListener listener:listenerList){
            listener.onConnect();
        }
    }

    @Override
    public void onLogin() {
        for(ChannelListener listener:listenerList){
            listener.onLogin();
        }
    }

    @Override
    public void onDisconnect() {
        for (int i = 0; i < listenerList.size(); i++) {
            listenerList.get(i).onDisconnect();
        }
    }

    @Override
    public void onConnecting() {
        for(ChannelListener listener:listenerList){
            listener.onConnecting();
        }
    }

}
