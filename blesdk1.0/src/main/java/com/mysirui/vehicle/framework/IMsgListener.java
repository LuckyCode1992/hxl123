package com.mysirui.vehicle.framework;

/**
 * 消息过滤和监听
 * Created by marlin on 2017/5/23.
 */

public interface IMsgListener<T> {

    boolean isMyMsg(T msg);

    boolean willUnReg();

    void handle(T msg);

}
