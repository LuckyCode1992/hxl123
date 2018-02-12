package com.mysirui.vehicle.framework;

import com.mysirui.vehicle.constants.BleErrorEnum;
import com.mysirui.vehicle.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提供 链接  断开  发送数据 和接收数据的基础功能
 * 子类为TCP 和 蓝牙
 * Created by marlin on 2017/5/24.
 */

public abstract class RawChannel<T> {


    public RawChannel(ChannelListener<T> listener, MsgCoder<T> coder) {
        this.listener = listener;
        this.coder = coder;
    }

    public static final int STATE_DISCONNTCTED = 0;
    public static final int STATE_CONNTCTING = 1;
    public static final int STATE_CONNTCTED = 2;

    int connStatus = STATE_DISCONNTCTED;

    protected ExecutorService threadPool = Executors.newFixedThreadPool(1);

    protected ChannelListener<T> listener = null;
    protected MsgCoder<T> coder;

    protected boolean shouldConn = false;

    public void reConnect(){
        this.dicConn();
        this.connIfNeeded();
    }

    public void connIfNeeded(){
        if(shouldConn){
            conn_internal();
        }
    }

    public void stop(){
        shouldConn = false;
        dicConn();
    }

    public void start(){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                if(!shouldConn){
                    shouldConn = true;
                    conn_internal();
                }
            }
        });
    }

    protected abstract boolean conn_internal();

    public void dicConn(){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                close_internal();
            }
        });
    }

    public void disConnWhenError(BleErrorEnum error){
        LogUtil.ble("主动断开:"+error);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                close_internal();
            }
        });
    }

    protected abstract void close_internal();

    public void send(final T msg){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                if(shouldConn && isConnected()){
                    latestSendTime = System.currentTimeMillis();
                    listener.willSend(msg);
                    send_internal(msg);
                }
            }
        });
    }

    protected abstract void send_internal(T msg);

    protected boolean changeStateTo(int newState){
        if(connStatus == newState ){
            return false;
        }
        connStatus = newState;
        if(connStatus == STATE_CONNTCTED){
            resetResource();
            listener.onConnect();
        }else if(connStatus == STATE_DISCONNTCTED){
            resetResource();
            listener.onDisconnect();
        }
        return true;

    }

    public void setLogined(){
        if(isConnected()){
            listener.onLogin();
        }
    }

    public boolean isConnected(){
        return connStatus == STATE_CONNTCTED;
    }
    public boolean isConnectedOrConnecting(){
        return connStatus !=STATE_DISCONNTCTED;
    }

    protected long latestReceiveTime = 0;
    protected long latestSendTime = 0;
    public long getLatestReceiveTime() {
        return latestReceiveTime;
    }
    public long getLatestSendTime() {
        return latestSendTime;
    }

    protected  void resetResource(){
        coder.resetResource();
    }

}
