package com.mysirui.vehicle.framework;


import com.mysirui.vehicle.BleRawClient;

public class BLEStringChannel extends MsgChannel<String> {

    private BleRawClient<String> bleClient;
    public BLEStringChannel(BleRawClient<String> client) {
        super(client, null);
        bleClient = client;
    }

    public void sendMsg(final String msg) {
       super.sendMsg(msg);
    }
}
