package com.mysirui.vehicle.framework;

/**
 * 只执行一次的消息监听
 * Created by marlin on 2017/5/24.
 */

public abstract class AlwaysMsgListener<T> implements IMsgListener<T> {

    @Override
    public boolean willUnReg() {
        return false;
    }

}
