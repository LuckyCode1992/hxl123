package com.mysirui.vehicle.framework;

/**
 * Created by marlin on 2017/5/23.
 * TCP 事件处理接口
 */

public interface ChannelEventListener {

    /*
    连接
     */
    void onConnect();

    /**
     *  认证成功
     */
    void onLogin();

    /*
    断开
     */
    void onDisconnect();

    /*
    正在连接
     */
    void onConnecting();

}
