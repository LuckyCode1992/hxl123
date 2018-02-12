package com.mysirui.vehicle.framework;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 消息编解码
 * Created by marlin on 2017/5/23.
 */

public interface MsgCoder<T>  {

    /**心跳
     * @return
     */
    T buildHeartMsg();

    /** 连接上服务端后首个消息
     * @return
     */
    T buildLoginMsg();

    List<T> parseMsg(byte[] byteMsg);

    ByteBuffer buildMsg(T msg);

    boolean isResponseOf(T sendMsg, T receiveiMsg);

    boolean isNotSupportAck(T sendMsg,T  receiveiMsg);

    /** 构建响应消息
     * @param pushMsg
     * @return
     */
    T buildResponse(T pushMsg);

    /**
     * 重置资源
     */
    void resetResource();

}
