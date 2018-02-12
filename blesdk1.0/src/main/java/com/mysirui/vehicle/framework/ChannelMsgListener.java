package com.mysirui.vehicle.framework;

/**
 * Channel上行事件监听
 * Created by marlin on 2017/5/23.
 */

public interface ChannelMsgListener<T> {
    /*
    接受到数据
     */
    void onReceive(T receiveiMsg);


    /**
     * @param sendMsg 将要发送的消息
     */
    void willSend(T sendMsg);

}
