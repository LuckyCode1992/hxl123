package com.mysirui.vehicle.framework;

/**
 * 只执行一次的消息监听
 * Created by marlin on 2017/5/24.
 */

public abstract class OnceMsgListener implements IMsgListener {


    boolean executed = false;

    @Override
    public boolean willUnReg() {
        return executed;
    }

    @Override
    public void handle(Object msg) {
        executed = true;
    }
}
