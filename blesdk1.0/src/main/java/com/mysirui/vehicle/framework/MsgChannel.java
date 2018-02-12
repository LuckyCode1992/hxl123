package com.mysirui.vehicle.framework;


import com.mysirui.vehicle.util.CollectionUtil;
import com.mysirui.vehicle.util.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 基于消息的队列
 * Created by marlin on 2017/5/23.
 */

public class MsgChannel<T> implements ChannelListener<T> {

    protected RawChannel<T> client;
    protected MsgCoder<T> coder;

    public MsgChannel(RawChannel<T> client, MsgCoder<T> coder) {
        this.client = client;
        this.coder = coder;
    }

    private List<IMsgListener> listeners = new ArrayList<IMsgListener>();

    public MsgChannel<T> addMsgListener(IMsgListener listener){
        listeners.add(listener);
        return this;
    }


    private Map<T,Subscriber<? super MsgResult<T>>> subscriberHashMap = new HashMap<T,Subscriber<? super MsgResult<T>>>();

    private void clearRequestWhenDisconn(){
        for(Subscriber<? super MsgResult<T>> subscriber: subscriberHashMap.values()){
            RxUtil.finish(subscriber,(MsgResult<T>) MsgResult.notConnected2Server);
        }
        subscriberHashMap.clear();
    }

    static Timer timer = new Timer();

    public void sendMsg(final T msg){
        client.send(msg);
    }

    public Observable<MsgResult<T>> sendMsg(final T msg, final int timeOut){
        return Observable.create(new Observable.OnSubscribe<MsgResult<T>>() {
            @Override
            public void call(final Subscriber<? super MsgResult<T>> subscriber) {
                if(!client.isConnected()){
                    RxUtil.finish(subscriber,(MsgResult<T>) MsgResult.notConnected2Server);
                }else{
                    subscriberHashMap.put(msg,subscriber);
                    client.send(msg);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(subscriberHashMap.containsValue(subscriber)){
                                RxUtil.finish(subscriber,(MsgResult<T>) MsgResult.TIME_OUT);
                                subscriberHashMap.remove(subscriber);
                            }
                        }
                    },timeOut);
                }
            }
        });
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onConnect() {
        clearRequestWhenDisconn();
    }

    @Override
    public void onDisconnect() {
        clearRequestWhenDisconn();
    }

    @Override
    public void onConnecting() {

    }

    @Override
    public void onReceive(T msg) {
        for(IMsgListener listener:listeners){
            if(listener.isMyMsg(msg)){
                listener.handle(msg);
            }
        }

        for(Object sendMsg: subscriberHashMap.keySet().toArray()){
            if(coder.isResponseOf((T)sendMsg,msg)){
                if(coder.isNotSupportAck((T)sendMsg,msg)){
                    RxUtil.finish(subscriberHashMap.get(sendMsg),(MsgResult<T>) MsgResult.notSupportTag);
                    subscriberHashMap.remove(sendMsg);
                }else {
                    MsgResult<T> result = new MsgResult<T>();
                    result.setResultEntity(msg);
                    RxUtil.finish(subscriberHashMap.get(sendMsg),result);
                    subscriberHashMap.remove(sendMsg);
                }
            }
        }

        CollectionUtil.remove(listeners, new Func1<IMsgListener, Boolean>() {
            @Override
            public Boolean call(IMsgListener listener) {
                return listener.willUnReg();
            }
        });
    }

    @Override
    public void willSend(T sendMsg) {

    }
}
