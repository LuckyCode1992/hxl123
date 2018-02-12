package com.mysirui.vehicle.framework;

import com.mysirui.vehicle.util.AndroidUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by marlin on 2017/5/23.
 * 默认链接管理器
 * 在前台并且登录后则连接、退出登录或者切换到后台断开连接
 */

public class ChannelConnectionManager<T> implements ChannelListener<T>{

    RawChannel<T> tcpRawClient = null;
    MsgCoder<T> coder = null;

    public ChannelConnectionManager(RawChannel tcpRawClient, MsgCoder coder) {
        this.tcpRawClient = tcpRawClient;
        this.coder = coder;
        timer.schedule(tcpCheckTimer,checkInterval,checkInterval);
    }

    private boolean closeWhenInBackground = true;

    public void setCloseWhenInBackground(boolean closeWhenInBackground) {
        this.closeWhenInBackground = closeWhenInBackground;
    }

    int retryCount = 0;
    @Override
    public void onConnect() {
        retryCount = 0;
    }


    @Override
    public void onDisconnect() {
        if(needConn()){ //
            retryCount ++;
            if(retryCount <= 3){ //重试3次，之后使用tcpCheckTimer 重连
                //tcpRawClient.connIfNeeded();
            }
        }
    }

    @Override
    public void onLogin() {

    }

    static Timer timer = new Timer();

    TimerTask tcpCheckTimer = new TimerTask() {
        @Override
        public void run() {
            if(!tcpRawClient.isConnected() ){
                if(isOnforceground() || !closeWhenInBackground){
                    tcpRawClient.connIfNeeded();
                }
            }else{
                if(!isOnforceground() && closeWhenInBackground){
                    tcpRawClient.dicConn();
                }else {  // 心跳检查
                    if(System.currentTimeMillis() - tcpRawClient.getLatestReceiveTime() > 3*heartInterval){
                        tcpRawClient.dicConn();  //心跳超时，断开
                    }else if(System.currentTimeMillis() - tcpRawClient.getLatestSendTime() > heartInterval){
                        tcpRawClient.send(coder.buildHeartMsg()); //发送心跳
                    }
                }
            }
        }
    };
    static final int checkInterval = 5000;//心跳间隔 5秒
    static final int heartInterval = 30000;//心跳间隔 30秒

    private boolean isOnforceground(){
        return AndroidUtil.isAPPForceground();
    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onReceive(T msg) {
        
    }

    @Override
    public void willSend(T sendMsg) {

    }

    private boolean needConn(){
        //return BaseApp.getInstance().isLogin() && BaseApp.getInstance().isAppForground() && AndroidUtils.isNetworkOK();
        return true;
    }

}
