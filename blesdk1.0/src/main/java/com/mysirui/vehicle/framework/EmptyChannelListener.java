package com.mysirui.vehicle.framework;

/**
 * Created by marlin on 2017/7/18.
 */

public class EmptyChannelListener<T> implements ChannelListener<T> {
    @Override
    public void onConnect() {

    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onReceive(T receiveiMsg) {

    }

    @Override
    public void willSend(T sendMsg) {

    }
}
